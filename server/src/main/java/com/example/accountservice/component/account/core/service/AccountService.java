package com.example.accountservice.component.account.core.service;

import com.example.accountservice.common.exception.InternalException;
import com.example.accountservice.component.account.core.dto.Account;
import com.example.accountservice.component.account.core.dto.AccountOperation;
import com.example.accountservice.component.account.core.repository.AccountRepository;
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

    private final AccountRepository accountRepository;

    private final AtomicLong unreadCounter = new AtomicLong();

    public AccountService(AccountCache accountCache,
                          KafkaTemplate<Object, Object> kafkaTemplate,
                          AccountRepository accountRepository) {
        this.accountCache = accountCache;
        this.kafkaTemplate = kafkaTemplate;
        this.accountRepository = accountRepository;
    }

    public Account getAccount(Integer id) {
        return accountCache.getAccount(id);
    }

    public void bufferAccountOperation(AccountOperation operation) {
        if (unreadCounter.incrementAndGet() > 5000) {
            unreadCounter.decrementAndGet();
            throw new InternalException("Operation can't be performed due to high load");
        }

        accountCache.compute(operation.getId(), existingAccount -> {
            Account updated = operation.apply(existingAccount);
            kafkaTemplate.send(KafkaConfig.TOPIC_NAME, updated);
            return updated;
        });
    }

    // TODO: add cache population after restart, ignore any requests during that time
    @KafkaListener(topics = KafkaConfig.TOPIC_NAME)
    public void applyAccountOperation(List<Account> updatedAccounts) {
        unreadCounter.updateAndGet(value -> value - updatedAccounts.size());
        accountRepository.batchReplace(updatedAccounts);
    }

}
