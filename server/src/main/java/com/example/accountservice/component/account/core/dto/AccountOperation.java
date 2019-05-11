package com.example.accountservice.component.account.core.dto;

public interface AccountOperation {

    Integer getId();

    Account apply(Account existing);

}
