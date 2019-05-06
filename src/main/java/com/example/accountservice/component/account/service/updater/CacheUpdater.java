package com.example.accountservice.component.account.service.updater;

import com.example.accountservice.component.account.repository.InMemoryCache;
import org.springframework.stereotype.Component;

@Component
public class CacheUpdater implements AccountUpdater {

    private final InMemoryCache inMemoryCache;

    public CacheUpdater(InMemoryCache inMemoryCache) {
        this.inMemoryCache = inMemoryCache;
    }

    @Override
    public void addAmount(Integer id, Long value) {
        inMemoryCache.addAmount(id, value);
    }

    @Override
    public Integer getOrdering() {
        return 0;
    }
}
