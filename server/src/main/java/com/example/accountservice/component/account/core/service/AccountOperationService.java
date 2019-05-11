package com.example.accountservice.component.account.core.service;

import com.example.accountservice.component.account.core.dto.AccountOperation;

import java.util.List;

public interface AccountOperationService {

    void applyFast(AccountOperation operation);

    void applyLong(List<AccountOperation> operations);

}
