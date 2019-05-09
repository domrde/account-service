package com.example.accountservice.component.client.service;

import com.example.accountservice.WiremockTest;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WiremockTest(stubs = "classpath:/mock-server")
public class AmountClientsTest {

    @Value("${wiremock.server.port}")
    private Integer wiremockPort;

    @Autowired
    private RestTemplate restTemplate;

    @After
    public void tearDown() {
        WireMock.resetAllScenarios();
    }

    @Test
    public void readerShouldRequestInitialAmount() {
        AmountReader reader = new AmountReader("http://localhost:" + wiremockPort, restTemplate);

        long amount = reader.readAmount(1);

        assertEquals(0L, amount);
    }

    @Test
    public void readerShouldRequestAmountUpdatedByWriter() {
        AmountReader reader = new AmountReader("http://localhost:" + wiremockPort, restTemplate);
        AmountWriter writer = new AmountWriter("http://localhost:" + wiremockPort, restTemplate);

        writer.writeAmount(1, 100L);
        long amount = reader.readAmount(1);

        assertEquals(100L, amount);
    }

}
