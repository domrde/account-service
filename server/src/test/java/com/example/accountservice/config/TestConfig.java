package com.example.accountservice.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.internal.stubbing.answers.DoesNothing.doesNothing;

@TestConfiguration
public class TestConfig {

    public static final String NO_KAFKA_PROFILE = "noKafka";

    // @MockBean marks context as dirty, but context reload is not required for tests that don't use kafka
    @Profile(NO_KAFKA_PROFILE)
    @Bean
    public KafkaTemplate<Object, Object> kafkaTemplate() {
        return Mockito.mock(KafkaTemplate.class, doesNothing());
    }

}
