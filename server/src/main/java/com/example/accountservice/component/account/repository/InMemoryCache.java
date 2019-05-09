package com.example.accountservice.component.account.repository;

import com.example.accountservice.component.account.dto.Account;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryCache {

    private final Map<Integer, Account> cache = new ConcurrentHashMap<>();

    public void addAmount(Integer id, Long value) {
        cache.compute(id, (existingId, existingAccount) -> replaceExisting(existingId, existingAccount, value));
    }

    private Account replaceExisting(Integer existingId, Account existingAccount, Long newValue) {
        if (existingAccount == null) {
            return new Account(existingId, newValue);
        } else {
            return new Account(existingId, existingAccount.getValue() + newValue);
        }
    }

    public Optional<Account> retrieve(Integer id) {
        return Optional.ofNullable(cache.get(id));
    }
}
