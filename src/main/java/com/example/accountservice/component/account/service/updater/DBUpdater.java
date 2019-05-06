package com.example.accountservice.component.account.service.updater;

import com.example.accountservice.component.account.repository.AccountRepository;
import org.springframework.stereotype.Component;

@Component
public class DBUpdater implements AccountUpdater {

    private final AccountRepository accountRepository;

    public DBUpdater(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void addAmount(Integer id, Long value) {
        accountRepository.addAmount(id, value);
    }

    @Override
    public Integer getOrdering() {
        return 1;
    }
}
