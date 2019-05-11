package com.example.accountservice.component.account.operation.service.handler;

import com.example.accountservice.component.account.core.dto.AccountOperation;

import java.util.List;

public interface AccountOperationHandler {

    void applyFast(AccountOperation operations);

    void applyLong(List<AccountOperation> operations);

    Class<?> getHandledOperationType();

}
