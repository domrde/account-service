package com.example.accountservice.component.account.core.service;

import com.example.accountservice.component.account.core.dto.Account;
import com.example.accountservice.component.account.core.repository.AccountRepository;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class AccountCache {

    private final AccountRepository accountRepository;

    private final Cache<Integer, Account> cache;

    public AccountCache(AccountRepository accountRepository) {
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


    public void updateCachedAccount(Integer id, Function<Account, Account> updater) {
        cache.asMap().compute(id, (existingId, existingAccount) -> updateCachedAccount(existingId, existingAccount, updater));
    }

    private Account updateCachedAccount(Integer id, Account existing, Function<Account, Account> updater) {
        if (existing == null) {
            existing = getAccountFromDB(id);
        }

        return updater.apply(existing);
    }

}
