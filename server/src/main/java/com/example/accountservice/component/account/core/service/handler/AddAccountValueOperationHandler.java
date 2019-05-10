package com.example.accountservice.component.account.core.service.handler;

import com.example.accountservice.component.account.core.dto.operation.AccountOperation;
import com.example.accountservice.component.account.core.dto.operation.AddAccountValueOperation;
import com.example.accountservice.component.account.core.service.CachedAccountService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Component
public class AddAccountValueOperationHandler implements AccountOperationHandler {

    private final CachedAccountService cachedAccountService;

    public AddAccountValueOperationHandler(CachedAccountService cachedAccountService) {
        this.cachedAccountService = cachedAccountService;
    }

    @Override
    public void handle(List<? extends AccountOperation> operations) {
        List<AddAccountValueOperation> addOperations = operations.stream()
                .filter(AddAccountValueOperation.class::isInstance)
                .map(op -> (AddAccountValueOperation) op)
                .collect(toList());

        Map<Integer, AddAccountValueOperation> mergedIncrements = addOperations.stream()
                .collect(toMap(
                        AddAccountValueOperation::getId,
                        Function.identity(),
                        (a, b) -> new AddAccountValueOperation(a.getId(), a.getValue() + b.getValue())
                ));

        cachedAccountService.addAmount(mergedIncrements.values());
    }

    @Override
    public Class<?> getHandledOperationType() {
        return AddAccountValueOperation.class;
    }
}
