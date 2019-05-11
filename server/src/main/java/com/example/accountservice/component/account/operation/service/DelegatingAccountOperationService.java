package com.example.accountservice.component.account.operation.service;

import com.example.accountservice.component.account.core.dto.AccountOperation;
import com.example.accountservice.component.account.core.service.AccountOperationService;
import com.example.accountservice.component.account.operation.service.handler.AccountOperationHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toConcurrentMap;

@Service
public class DelegatingAccountOperationService implements AccountOperationService {

    private final Map<Class<?>, AccountOperationHandler> handlersByType;

    public DelegatingAccountOperationService(List<AccountOperationHandler> operationHandlers) {
        handlersByType = operationHandlers.stream().collect(toConcurrentMap(
                AccountOperationHandler::getHandledOperationType,
                Function.identity()
        ));
    }

    @Override
    public void applyFast(AccountOperation operation) {
        Optional.ofNullable(handlersByType.get(operation.getClass()))
                .ifPresent(handler -> handler.applyFast(operation));
    }

    @Override
    public void applyLong(List<AccountOperation> operations) {
        Map<Class<?>, List<AccountOperation>> operationsByClass =
                operations.stream().collect(groupingBy(Object::getClass));

        operationsByClass.forEach((type, ops) ->
                Optional.ofNullable(handlersByType.get(type))
                        .ifPresent(handler -> handler.applyLong(ops))
        );
    }
}
