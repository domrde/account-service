package com.example.accountservice.component.account.core.operation;

import com.example.accountservice.component.account.core.dto.Account;

public interface AccountOperation {

    Integer getId();

    Account apply(Account existing);

}
