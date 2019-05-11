package com.example.accountservice.component.account.core.service;

import com.example.accountservice.common.exception.InternalException;
import com.example.accountservice.component.account.core.dto.Account;
import com.example.accountservice.component.account.core.dto.AccountOperation;
import com.example.accountservice.system.config.KafkaConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AccountService {

    private final AccountCache accountCache;

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    private final AccountOperationService accountOperationService;

    private final AtomicLong unreadCounter = new AtomicLong();

    public AccountService(AccountCache accountCache,
                          KafkaTemplate<Object, Object> kafkaTemplate,
                          AccountOperationService accountOperationService) {
        this.accountCache = accountCache;
        this.kafkaTemplate = kafkaTemplate;
        this.accountOperationService = accountOperationService;
    }

    public Account getAccount(Integer id) {
        return accountCache.getAccount(id);
    }

    public void bufferAccountOperation(AccountOperation operation) {
        if (unreadCounter.incrementAndGet() > 5000) {
            unreadCounter.decrementAndGet();
            throw new InternalException("Operation can't be performed due to high load");
        }

        // TODO: switch places and send not an operation to kafka but already updated account
        accountOperationService.applyFast(operation);
        kafkaTemplate.send(KafkaConfig.TOPIC_NAME, operation);
    }

    // TODO: add cache population after restart
    @KafkaListener(topics = KafkaConfig.TOPIC_NAME)
    public void applyAccountOperation(List<AccountOperation> operations) {
        unreadCounter.updateAndGet(value -> value - operations.size());
        accountOperationService.applyLong(operations);
    }

}
