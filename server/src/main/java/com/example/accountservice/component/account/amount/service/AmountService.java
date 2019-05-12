package com.example.accountservice.component.account.amount.service;

import com.example.accountservice.component.account.core.operation.AddAccountValueOperation;
import com.example.accountservice.component.account.core.service.AccountService;
import org.springframework.stereotype.Service;

@Service
public class AmountService {

    private final AccountService accountService;

    public AmountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public Long getAmount(Integer id) {
        return accountService.getAccount(id).getValue();
    }

    public void addAmount(Integer id, Long value) {
        accountService.bufferAccountOperation(new AddAccountValueOperation(id, value));
    }

}
