package com.example.accountservice.system.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
@ConditionalOnProperty(name = "kafka.enable", havingValue = "true", matchIfMissing = true)
public class KafkaConfig {

    public static final String TOPIC_NAME = "account";

}
