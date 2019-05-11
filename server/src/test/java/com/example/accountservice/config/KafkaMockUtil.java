package com.example.accountservice.config;

import com.example.accountservice.component.account.core.dto.AccountOperation;
import com.example.accountservice.component.account.core.service.AccountService;
import org.springframework.kafka.core.KafkaTemplate;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class KafkaMockUtil {

    public static void mockRedirectToAccountService(KafkaTemplate<Object, Object> kafkaTemplate,
                                                    AccountService accountService) {
        when(kafkaTemplate.send(anyString(), any(AccountOperation.class))).thenAnswer(invocation -> {
            AccountOperation operation = invocation.getArgument(1);
            accountService.applyAccountOperation(singletonList(operation));
            return null; // not used
        });
    }

    private KafkaMockUtil() {
    }
}
