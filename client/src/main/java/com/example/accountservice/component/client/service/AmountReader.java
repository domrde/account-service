package com.example.accountservice.component.client.service;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class AmountReader {

    private final String targetHost;

    private final RestTemplate restTemplate;

    public AmountReader(String targetHost, RestTemplate restTemplate) {
        this.targetHost = targetHost;
        this.restTemplate = restTemplate;
    }

    public Long readAmount(Integer id) {
        String url = UriComponentsBuilder
                .fromHttpUrl(targetHost + "/account/" + id)
                .toUriString();

        return restTemplate.getForEntity(url, Long.class).getBody();
    }

}
