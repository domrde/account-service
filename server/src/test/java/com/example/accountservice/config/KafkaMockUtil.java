package com.example.accountservice.config;

import com.example.accountservice.component.account.core.dto.Account;
import com.example.accountservice.component.account.core.service.AccountService;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.atomic.AtomicLong;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class KafkaMockUtil {

    private static final AtomicLong offset = new AtomicLong();

    public static void mockRedirectToAccountService(KafkaTemplate<Object, Object> kafkaTemplate,
                                                    AccountService accountService) {
        when(kafkaTemplate.send(anyString(), any(Account.class))).thenAnswer(invocation -> {
            Account account = invocation.getArgument(1);
            accountService.applyAccountOperation(singletonList(account), singletonList(offset.incrementAndGet()));
            return null; // not used
        });
    }

    private KafkaMockUtil() {
    }
}
