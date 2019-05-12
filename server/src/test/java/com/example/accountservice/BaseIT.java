package com.example.accountservice;

import com.example.accountservice.component.account.core.service.AccountService;
import com.example.accountservice.config.KafkaMockUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

public class BaseIT {

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @Autowired
    private AccountService accountService;

    @Rule
    public TestName testName = new TestName();

    protected Integer accountId;

    @Before
    public void init() {
        accountId = testName.getMethodName().hashCode();
        KafkaMockUtil.mockRedirectToAccountService(kafkaTemplate, accountService);
    }
}
