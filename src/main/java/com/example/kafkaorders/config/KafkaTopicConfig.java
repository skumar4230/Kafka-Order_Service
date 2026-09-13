package com.example.kafkaorders.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    NewTopic ordersTopic(@Value("${app.kafka.topics.orders}") String name) {
        return new NewTopic(name, 3, (short) 1);
    }

    @Bean
    NewTopic ordersRetryTopic(@Value("${app.kafka.topics.orders-retry}") String name) {
        return new NewTopic(name, 3, (short) 1);
    }

    @Bean
    NewTopic ordersDltTopic(@Value("${app.kafka.topics.orders-dlt}") String name) {
        return new NewTopic(name, 3, (short) 1);
    }
}
