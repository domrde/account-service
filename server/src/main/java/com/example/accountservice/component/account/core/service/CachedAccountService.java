package com.example.accountservice.component.account.core.service;

import com.example.accountservice.component.account.core.dto.Account;
import com.example.accountservice.component.account.core.dto.operation.AddAccountValueOperation;
import com.example.accountservice.component.account.core.repository.AccountRepository;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CachedAccountService {

    private final AccountRepository accountRepository;

    private final Cache<Integer, Account> cache;

    public CachedAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();

    }

    public Account getAccount(Integer id) {
        return cache.get(id, this::getAccountFromDB);
    }

    private Account getAccountFromDB(Integer id) {
        return accountRepository.getAccount(id).orElseGet(() -> new Account(id, 0L));
    }

    public void addAmount(Collection<AddAccountValueOperation> addOperations) {
        List<Account> batch = addOperations.stream()
                .map(operation ->
                        cache.asMap().compute(
                                operation.getId(),
                                (existingId, existingAccount) -> applyNewValue(existingId, existingAccount, operation.getValue())
                        )
                )
                .collect(Collectors.toList());

        accountRepository.replace(batch);
    }

    private Account applyNewValue(Integer id, Account existing, Long valueToAdd) {
        if (existing == null) {
            return new Account(id, valueToAdd);
        } else {
            return new Account(id, existing.getValue() + valueToAdd);
        }
    }
}
