package com.example.accountservice.component.metric.api;

import com.example.accountservice.component.metric.service.MetricService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.accountservice.common.URLs.METRIC_URL;

@RestController
@RequestMapping(METRIC_URL)
public class MetricController {

    private final MetricService metricService;

    public MetricController(MetricService metricService) {
        this.metricService = metricService;
    }

    @GetMapping(value = "/retrieve/{timeWindow}/minutes", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getRetrieveMetric(@PathVariable int timeWindow) {
        return metricService.createGraph(MetricService.RETRIEVE_METRIC_NAME, timeWindow);
    }

    @GetMapping(value = "/update/{timeWindow}/minutes", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getUpdateMetric(@PathVariable int timeWindow) {
        return metricService.createGraph(MetricService.UPDATE_METRIC_NAME, timeWindow);
    }
}
