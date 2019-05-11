package com.example.accountservice.component.account.core.service;

import com.example.accountservice.IntegrationTest;
import com.example.accountservice.component.account.core.dto.Account;
import com.example.accountservice.component.account.core.dto.AccountWithOffset;
import com.example.accountservice.component.account.core.repository.AccountRepository;
import com.example.accountservice.component.account.core.dto.AddAccountValueOperation;
import com.example.accountservice.config.KafkaMockUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.example.accountservice.config.TestConfig.NO_KAFKA_PROFILE;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@IntegrationTest
@ActiveProfiles(NO_KAFKA_PROFILE)
@RunWith(SpringRunner.class)
public class AccountServiceRetrievalIT {

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @Autowired
    private AccountService accountService;

    @SpyBean
    private AccountRepository accountRepository;

    @Rule
    public TestName testName = new TestName();

    private Integer accountId;

    @Before
    public void init() {
        accountId = testName.getMethodName().hashCode();
        KafkaMockUtil.mockRedirectToAccountService(kafkaTemplate, accountService);
    }

    @Test
    public void defaultValueShouldBeReturnedIfThereAreNoRecordsInDB() {
        Account account = accountService.getAccount(accountId);

        assertEquals(new Account(accountId, 0L), account);
        verify(accountRepository, times(1)).getAccount(accountId);
    }

    @Test
    public void valueShouldBeReturnedIfItIsInDB() {
        accountRepository.batchReplace(singletonList(new AccountWithOffset(accountId, 100L, 1L)));

        Account account = accountService.getAccount(accountId);

        assertEquals(new Account(accountId, 100L), account);
        verify(accountRepository, times(1)).getAccount(accountId);
    }

    @Test
    public void valueShouldBeReturnedIfItIsInCache() {
        accountService.bufferAccountOperation(new AddAccountValueOperation(accountId, 100L));

        Account account = accountService.getAccount(accountId);

        assertEquals(new Account(accountId, 100L), account);
        verify(accountRepository, times(1)).getAccount(accountId);
    }

    @Test
    public void valueInCacheShouldBeIncrementedBasedOnDBValueIfCacheIsEmpty() {
        accountRepository.batchReplace(singletonList(new AccountWithOffset(accountId, 100L, 1L)));
        accountService.bufferAccountOperation(new AddAccountValueOperation(accountId, 100L));

        Account account = accountService.getAccount(accountId);

        assertEquals(new Account(accountId, 200L), account);
        verify(accountRepository, times(1)).getAccount(accountId);
    }

    @Test
    public void valueInCacheShouldBeIncrementedBasedOnCacheValue() {
        accountRepository.batchReplace(singletonList(new AccountWithOffset(accountId, 100L, 1L)));
        accountService.bufferAccountOperation(new AddAccountValueOperation(accountId, 100L));
        accountService.bufferAccountOperation(new AddAccountValueOperation(accountId, 100L));

        Account account = accountService.getAccount(accountId);

        assertEquals(new Account(accountId, 300L), account);
        verify(accountRepository, times(1)).getAccount(accountId);
    }
}
