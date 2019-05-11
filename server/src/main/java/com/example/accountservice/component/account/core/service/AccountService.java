package com.example.accountservice.component.account.core.service;

import com.example.accountservice.common.exception.ServiceUnavailableException;
import com.example.accountservice.component.account.core.dto.Account;
import com.example.accountservice.component.account.core.dto.AccountOperation;
import com.example.accountservice.component.account.core.dto.AccountWithOffset;
import com.example.accountservice.component.account.core.repository.AccountRepository;
import com.example.accountservice.system.config.KafkaConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.example.accountservice.component.account.core.service.RequestProcessingBlocker.KAFKA_LISTENER_ID;

@Service
public class AccountService {

    private static final int MAXIMUM_UNREAD_ACCOUNTS_NUMBER = 5000;

    private final AccountCache accountCache;

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    private final AccountRepository accountRepository;

    private final RequestProcessingBlocker requestProcessingBlocker;

    private final AtomicLong unreadCounter = new AtomicLong();

    public AccountService(AccountCache accountCache,
                          KafkaTemplate<Object, Object> kafkaTemplate,
                          AccountRepository accountRepository,
                          RequestProcessingBlocker requestProcessingBlocker) {
        this.accountCache = accountCache;
        this.kafkaTemplate = kafkaTemplate;
        this.accountRepository = accountRepository;
        this.requestProcessingBlocker = requestProcessingBlocker;
    }

    public Account getAccount(Integer id) {
        return accountCache.getAccount(id);
    }

    public void bufferAccountOperation(AccountOperation operation) {
        requestProcessingBlocker.assertCanStartProcessing();

        if (unreadCounter.incrementAndGet() > MAXIMUM_UNREAD_ACCOUNTS_NUMBER) {
            unreadCounter.decrementAndGet();
            throw new ServiceUnavailableException("Operation can't be performed due to high load");
        }

        accountCache.compute(operation.getId(), existingAccount -> {
            Account updated = operation.apply(existingAccount);
            kafkaTemplate.send(KafkaConfig.TOPIC_NAME, updated);
            return updated;
        });
    }

    @KafkaListener(id = KAFKA_LISTENER_ID, topics = KafkaConfig.TOPIC_NAME)
    public void applyAccountOperation(List<Account> updatedAccounts,
                                      @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        unreadCounter.updateAndGet(value -> value > 0 ? value - updatedAccounts.size() : value);

        List<AccountWithOffset> accounts = new ArrayList<>();
        for (int i = 0; i < updatedAccounts.size(); i++) {
            Account account = updatedAccounts.get(i);
            Long offset = offsets.get(i);
            accounts.add(new AccountWithOffset(account.getId(), account.getValue(), offset));
        }

        accountRepository.batchReplace(accounts);
    }

}
