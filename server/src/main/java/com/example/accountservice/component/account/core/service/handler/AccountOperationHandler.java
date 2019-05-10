package com.example.accountservice.component.account.core.service.handler;

import com.example.accountservice.component.account.core.dto.operation.AccountOperation;

import java.util.List;

public interface AccountOperationHandler {

    void handle(List<? extends AccountOperation> operations);

    Class<?> getHandledOperationType();

}
