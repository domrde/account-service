package com.example.accountservice.component.account.amount.api;

import com.example.accountservice.IntegrationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.example.accountservice.config.TestConfig.NO_KAFKA_PROFILE;
import static org.junit.Assert.assertEquals;

@IntegrationTest
@ActiveProfiles(NO_KAFKA_PROFILE)
@RunWith(SpringRunner.class)
public class AmountControllerIT {

    @Autowired
    private AmountControllerTestHelper amountControllerTestHelper;

    @Test
    public void valueShouldBeZeroForNotUpdatedAccount() {
        assertEquals(0L, amountControllerTestHelper.getAmount(1));
    }

    @Test
    public void valueShouldBeSavedOnUpdate() {
        amountControllerTestHelper.addAmount(1, 100L);
        assertEquals(100L, amountControllerTestHelper.getAmount(1));
    }

    @Test
    public void valueShouldBeAccumulated() {
        amountControllerTestHelper.addAmount(2, 100L);
        amountControllerTestHelper.addAmount(2, 100L);
        amountControllerTestHelper.addAmount(2, 100L);
        assertEquals(300L, amountControllerTestHelper.getAmount(2));
    }

}


