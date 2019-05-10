package com.example.accountservice.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaConfig {

    public static final String TOPIC_NAME = "account";

}
