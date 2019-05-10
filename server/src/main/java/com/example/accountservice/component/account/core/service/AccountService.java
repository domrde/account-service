package com.example.accountservice.component.account.core.service;

import com.example.accountservice.common.exception.InternalException;
import com.example.accountservice.component.account.core.dto.Account;
import com.example.accountservice.component.account.core.dto.operation.AccountOperation;
import com.example.accountservice.component.account.core.service.handler.AccountOperationHandler;
import com.example.accountservice.system.config.KafkaConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toConcurrentMap;

@Service
public class AccountService {

    private final CachedAccountService cachedAccountService;

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    private final Map<Class<?>, AccountOperationHandler> handlerCache;

    private final AtomicLong unreadCounter = new AtomicLong();

    public AccountService(CachedAccountService cachedAccountService,
                          KafkaTemplate<Object, Object> kafkaTemplate,
                          List<AccountOperationHandler> operationHandlers) {
        this.cachedAccountService = cachedAccountService;
        this.kafkaTemplate = kafkaTemplate;

        handlerCache = operationHandlers.stream().collect(toConcurrentMap(
                AccountOperationHandler::getHandledOperationType,
                Function.identity()
        ));
    }

    public Account getAccount(Integer id) {
        return cachedAccountService.getAccount(id);
    }

    public void bufferAccountOperation(AccountOperation operation) {
        if (unreadCounter.incrementAndGet() > 5000) {
            throw new InternalException("Operation can't be performed due to high load");
        }

        kafkaTemplate.send(KafkaConfig.TOPIC_NAME, operation);
    }

    @KafkaListener(topics = KafkaConfig.TOPIC_NAME)
    protected void applyAccountOperation(List<AccountOperation> operations) {
        unreadCounter.updateAndGet(value -> value - operations.size());

        Map<Class<?>, List<AccountOperation>> operationsByClass =
                operations.stream().collect(groupingBy(Object::getClass));

        operationsByClass.forEach((clazz, ops) ->
                Optional.ofNullable(handlerCache.get(clazz))
                        .ifPresent(handler -> handler.handle(ops))
        );
    }

}
