package com.example.accountservice.component.account.core.service;

import com.example.accountservice.common.exception.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.event.ListenerContainerIdleEvent;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Before new requests' processing can be started all messages that were in kafka before start of application
 * should be processed. That's why that Blocker waits for all concurrent kafka listeners to send Idle event.
 * Idle event is sent when listener didn't receive messages for spring.kafka.listener.idleEventInterval millis.
 */
@Service
public class RequestProcessingBlocker {

    public static final String KAFKA_LISTENER_ID = "account-service";

    private final Integer listenersNumber;

    private final Set<String> idleConsumers = ConcurrentHashMap.newKeySet();

    public RequestProcessingBlocker(@Value("${spring.kafka.listener.concurrency:0}") Integer listenersNumber) {
        this.listenersNumber = listenersNumber;
    }

    @EventListener(condition = "event.listenerId.startsWith('" + KAFKA_LISTENER_ID + "-')")
    public void eventHandler(ListenerContainerIdleEvent event) {
        idleConsumers.add(event.getListenerId());
    }

    public void assertCanStartProcessing() {
        if (idleConsumers.size() != listenersNumber) {
            throw new ServiceUnavailableException("Operation can't be performed due to service startup");
        }
    }
}
