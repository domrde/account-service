package com.example.accountservice.component.account.service;

import com.example.accountservice.component.account.dto.Account;
import com.example.accountservice.component.account.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Long getAmount(Integer id) {
        return accountRepository.getAmount(id)
                .map(Account::getValue)
                .orElse(0L);
    }

    public void addAmount(Integer id, Long value) {
        accountRepository.addAmount(id, value);
    }
}
