package com.example.accountservice.component.account.amount.api;

import com.example.accountservice.common.URLs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class AccountTestHelper {

    @Autowired
    private MockMvc mockMvc;

    public long getAmount(Integer id) {
        String responseAsString;
        try {
            responseAsString = mockMvc.perform(get(URLs.ACCOUNT_AMOUNT_URL + "/" + id))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return Long.valueOf(responseAsString);
    }

    public void addAmount(Integer id, Long value) {
        try {
            mockMvc.perform(post(URLs.ACCOUNT_AMOUNT_URL + "/" + id).param("value", String.valueOf(value)))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
