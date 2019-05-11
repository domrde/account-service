package com.example.accountservice.component.account.operation.service.handler;

import com.example.accountservice.component.account.core.dto.Account;
import com.example.accountservice.component.account.core.dto.AccountOperation;
import com.example.accountservice.component.account.core.repository.AccountRepository;
import com.example.accountservice.component.account.core.service.AccountCache;
import com.example.accountservice.component.account.operation.dto.AddAccountValueOperation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Component
public class AddAccountValueOperationHandler implements AccountOperationHandler {

    private final AccountCache accountCache;

    private final AccountRepository accountRepository;

    public AddAccountValueOperationHandler(AccountCache accountCache,
                                           AccountRepository accountRepository) {
        this.accountCache = accountCache;
        this.accountRepository = accountRepository;
    }

    @Override
    public void applyFast(AccountOperation operation) {
        if (operation instanceof AddAccountValueOperation) {
            AddAccountValueOperation addValueOperation = (AddAccountValueOperation) operation;
            Function<Account, Account> updated = (existing) ->
                    new Account(existing.getId(), existing.getValue() + addValueOperation.getValue());
            accountCache.updateCachedAccount(addValueOperation.getId(), updated);
        } else {
            throw new IllegalArgumentException(String.format(
                    "Unsupported operation supplied to handler. Expected [%s] but was [%s]",
                    getHandledOperationType().getSimpleName(),
                    operation.getClass().getSimpleName()
            ));
        }
    }

    @Override
    public void applyLong(List<AccountOperation> operations) {
        List<AddAccountValueOperation> addOperations = operations.stream()
                .filter(AddAccountValueOperation.class::isInstance)
                .map(op -> (AddAccountValueOperation) op)
                .collect(toList());

        Map<Integer, Long> mergedIncrements = addOperations.stream()
                .collect(toMap(
                        AddAccountValueOperation::getId,
                        AddAccountValueOperation::getValue,
                        Long::sum
                ));

        accountRepository.updateValue(mergedIncrements);
    }

    @Override
    public Class<?> getHandledOperationType() {
        return AddAccountValueOperation.class;
    }
}
