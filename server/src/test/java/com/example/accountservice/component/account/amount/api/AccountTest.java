package com.example.accountservice.component.account.amount.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(
        properties = {
                "management.metrics.enable.all=false",
                "management.metrics.export.atlas.enabled=false"
        })
@RunWith(SpringRunner.class)
public class AccountTest {

    @Autowired
    private AccountTestHelper accountTestHelper;

    @Test
    public void valueShouldBeZeroForNotUpdatedAccount() {
        assertEquals(0L, accountTestHelper.getAmount(1));
    }

    @Test
    public void valueShouldBeSavedOnUpdate() {
        accountTestHelper.addAmount(1, 100L);
        assertEquals(100L, accountTestHelper.getAmount(1));
    }

    @Test
    public void valueShouldBeAccumulated() {
        accountTestHelper.addAmount(2, 100L);
        accountTestHelper.addAmount(2, 100L);
        accountTestHelper.addAmount(2, 100L);
        assertEquals(300L, accountTestHelper.getAmount(2));
    }



}
