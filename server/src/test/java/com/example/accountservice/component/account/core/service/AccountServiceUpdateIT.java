package com.example.accountservice.component.account.core.service;

import com.example.accountservice.BaseIT;
import com.example.accountservice.IntegrationTest;
import com.example.accountservice.component.account.core.dto.Account;
import com.example.accountservice.component.account.core.dto.AccountWithOffset;
import com.example.accountservice.component.account.core.operation.AddAccountValueOperation;
import com.example.accountservice.component.account.core.repository.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.example.accountservice.config.TestConfig.NO_KAFKA_PROFILE;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@IntegrationTest
@ActiveProfiles(NO_KAFKA_PROFILE)
@RunWith(SpringRunner.class)
public class AccountServiceUpdateIT extends BaseIT {

    @Autowired
    private AccountService accountService;

    @SpyBean
    private AccountRepository accountRepository;

    @Test
    public void accountShouldBeCreatedIfItWasNotInDBForUpdate() {
        accountService.bufferAccountOperation(new AddAccountValueOperation(accountId, 100L));

        Optional<Account> accountOpt = accountRepository.getAccount(accountId);

        assertTrue(accountOpt.isPresent());
        assertEquals(new Account(accountId, 100L), accountOpt.get());
        verify(accountRepository, atLeastOnce()).batchReplace(anyList());
    }

    @Test
    public void existingAccountShouldHaveItsValueUpdated() {
        accountRepository.batchReplace(singletonList(new AccountWithOffset(accountId, 100L, 1L)));
        accountService.bufferAccountOperation(new AddAccountValueOperation(accountId, 100L));

        Optional<Account> accountOpt = accountRepository.getAccount(accountId);

        assertTrue(accountOpt.isPresent());
        assertEquals(new Account(accountId, 200L), accountOpt.get());
        verify(accountRepository, atLeastOnce()).batchReplace(anyList());
    }

}
