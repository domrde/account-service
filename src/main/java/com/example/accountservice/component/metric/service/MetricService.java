package com.example.accountservice.component.metric.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MetricService {

    public static final String RETRIEVE_METRIC_NAME = "custom.account.get-amount";

    public static final String UPDATE_METRIC_NAME = "custom.account.add-amount";

    private final RestTemplate restTemplate;

    private final String atlasUriString;

    public MetricService(RestTemplate restTemplate, @Value("${atlas.uri}") String atlasUriString) {
        this.restTemplate = restTemplate;
        this.atlasUriString = atlasUriString;
    }

    public byte[] createGraph(String metricName, int timeWindowMinutes) {
        String startTimeQuery = String.format("e-%dm", timeWindowMinutes);
        String graphQuery = String.format("name,%s,:eq,statistic,count,:eq,:and", metricName);

        String uriString = UriComponentsBuilder
                .fromHttpUrl(atlasUriString)
                .queryParam("s", startTimeQuery)
                .queryParam("tz", "Europe/Moscow")
                .queryParam("q", graphQuery)
                .toUriString();

        byte[] response = restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                null,
                byte[].class
        ).getBody();

        return response;
    }
}
