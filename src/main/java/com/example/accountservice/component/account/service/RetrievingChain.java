package com.example.accountservice.component.account.service;

import com.example.accountservice.component.account.dto.Account;
import com.example.accountservice.component.account.service.retriever.AccountRetriever;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RetrievingChain {

    private final List<AccountRetriever> retrievers;

    public RetrievingChain(List<AccountRetriever> retrievers) {
        this.retrievers = retrievers.stream()
                .sorted(Comparator.comparing(AccountRetriever::getOrdering))
                .collect(Collectors.toList());
    }

    public Optional<Account> retrieveAccount(Integer id) {
        return retrievers.stream()
                .map(r -> r.retrieve(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}
