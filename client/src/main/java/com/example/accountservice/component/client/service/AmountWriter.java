package com.example.accountservice.component.client.service;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class AmountWriter {

    private final String targetHost;

    private final RestTemplate restTemplate;

    public AmountWriter(String targetHost, RestTemplate restTemplate) {
        this.targetHost = targetHost;
        this.restTemplate = restTemplate;
    }

    public void writeAmount(Integer id, Long value) {
        String url = UriComponentsBuilder
                .fromHttpUrl(targetHost + "/account/amount/" + id)
                .queryParam("value", value)
                .toUriString();

        try {
            restTemplate.put(url, null, Void.class);
        } catch (Exception e) {
            // exceptions should not stop the load
        }
    }

}
