package com.example.accountservice.component.account.service.retriever;

import com.example.accountservice.component.account.dto.Account;
import com.example.accountservice.component.account.repository.AccountRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DBRetriever implements AccountRetriever {

    private final AccountRepository accountRepository;

    public DBRetriever(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<Account> retrieve(Integer id) {
        return accountRepository.retrieve(id);
    }

    @Override
    public Integer getOrdering() {
        return 1;
    }
}
