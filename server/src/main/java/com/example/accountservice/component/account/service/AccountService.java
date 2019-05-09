package com.example.accountservice.component.account.service;

import com.example.accountservice.component.account.dto.Account;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final RetrievingChain retrievingChain;

    private final UpdatingChain updatingChain;

    public AccountService(RetrievingChain retrievingChain, UpdatingChain updatingChain) {
        this.retrievingChain = retrievingChain;
        this.updatingChain = updatingChain;
    }

    public Long getAmount(Integer id) {
        return retrievingChain.retrieveAccount(id).map(Account::getValue).orElse(0L);
    }

    public void addAmount(Integer id, Long value) {
        updatingChain.addAmount(id, value);
    }
}
