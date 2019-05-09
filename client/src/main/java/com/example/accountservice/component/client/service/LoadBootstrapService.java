package com.example.accountservice.component.client.service;

import com.example.accountservice.component.client.util.IdRangeParsingUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Service
public class LoadBootstrapService {

    private final Boolean autoStartLoad;

    private final Integer readersNumber;

    private final Integer writersNumber;

    private final String targetHost;

    private final IdRangeParsingUtils.IdHolder idHolder;

    private final RestTemplate restTemplate;

    private final ExecutorService executorService;

    public LoadBootstrapService(@Value("${autostart.load}") Boolean autoStartLoad,
                                @Value("${reader.number}") Integer readersNumber,
                                @Value("${writer.number}") Integer writersNumber,
                                @Value("${target.host}") String targetHost,
                                @Value("${id.range}") String idRangeString,
                                RestTemplate restTemplate,
                                ExecutorService executorService) {
        this.autoStartLoad = autoStartLoad;
        this.readersNumber = readersNumber;
        this.writersNumber = writersNumber;
        this.targetHost = targetHost;
        this.idHolder = IdRangeParsingUtils.parseIdRangeString(idRangeString);
        this.restTemplate = restTemplate;
        this.executorService = executorService;
    }

    @PostConstruct
    public void start() {
        if (autoStartLoad) {
            applyLoad();
        }
    }

    private void applyLoad() {
        AmountReader amountReader = new AmountReader(targetHost, restTemplate);
        IntStream.range(0, readersNumber)
                .forEach(i -> applyReadLoad(amountReader));

        AmountWriter amountWriter = new AmountWriter(targetHost, restTemplate);
        IntStream.range(0, writersNumber)
                .forEach(i -> applyWriteLoad(amountWriter));
    }

    private void applyReadLoad(AmountReader amountReader) {
        CompletableFuture
                .runAsync(() -> amountReader.readAmount(idHolder.getRandomId()), executorService)
                .thenRunAsync(() -> applyReadLoad(amountReader), executorService);
    }

    private void applyWriteLoad(AmountWriter amountWriter) {
        CompletableFuture
                .runAsync(
                        () -> {
                            long randomAmount = ThreadLocalRandom.current().nextLong(500);
                            amountWriter.writeAmount(idHolder.getRandomId(), randomAmount);
                        },
                        executorService
                )
                .thenRunAsync(() -> applyWriteLoad(amountWriter), executorService);
    }
}
