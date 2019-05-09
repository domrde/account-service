package com.example.accountservice.component.account.service;

import com.example.accountservice.component.account.service.updater.AccountUpdater;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpdatingChain {

    private final List<AccountUpdater> updaters;

    public UpdatingChain(List<AccountUpdater> updaters) {
        this.updaters = updaters.stream()
                .sorted(Comparator.comparing(AccountUpdater::getOrdering))
                .collect(Collectors.toList());
    }

    public void addAmount(Integer id, Long value) {
        updaters.forEach(u -> u.addAmount(id, value));
    }
}
