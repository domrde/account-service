package com.example.accountservice.component.account.core.service;

import com.example.accountservice.common.exception.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.event.ListenerContainerIdleEvent;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class RequestProcessingBlocker {

    public static final String KAFKA_LISTENER_ID = "account-service";

    private final CountDownLatch startConsumingRequests;

    public RequestProcessingBlocker(@Value("${spring.kafka.listener.concurrency:0}") Integer listenersNumber) {
        this.startConsumingRequests = new CountDownLatch(listenersNumber);
    }

    @EventListener(condition = "event.listenerId.startsWith('" + KAFKA_LISTENER_ID + "-')")
    public void eventHandler(ListenerContainerIdleEvent event) {
        startConsumingRequests.countDown();
    }

    public void assertCanStartProcessing() {
        try {
            if (!startConsumingRequests.await(1, TimeUnit.SECONDS)) {
                throw new ServiceUnavailableException("Operation can't be performed due to service startup");
            }
        } catch (InterruptedException e) {
            throw new ServiceUnavailableException("Operation can't be performed due to service startup", e);
        }
    }
}
