package com.example.accountservice.component.account.service.retriever;

import com.example.accountservice.component.account.dto.Account;
import com.example.accountservice.component.account.repository.InMemoryCache;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CacheRetriever implements AccountRetriever {

    private final InMemoryCache inMemoryCache;

    public CacheRetriever(InMemoryCache inMemoryCache) {
        this.inMemoryCache = inMemoryCache;
    }

    @Override
    public Optional<Account> retrieve(Integer id) {
        return inMemoryCache.retrieve(id);
    }

    @Override
    public Integer getOrdering() {
        return 0;
    }
}
