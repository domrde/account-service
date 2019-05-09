package com.example.accountservice.component.account.service.retriever;

import com.example.accountservice.component.account.dto.Account;

import java.util.Optional;

public interface AccountRetriever {

    Optional<Account> retrieve(Integer id);

    Integer getOrdering();

}
