package com.example.accountservice.component.account.service;

import org.springframework.stereotype.Service;

@Service
public class BufferedAccountService {

    private final AccountService accountService;

    public BufferedAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public Long getAmount(Integer id) {
        return accountService.getAmount(id);
    }

    public void addAmount(Integer id, Long value) {
        accountService.addAmount(id, value);
    }

}
