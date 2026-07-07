package com.example.logisticstrackingservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic shipmentStatusEventsTopic() {
        return org.springframework.kafka.config.TopicBuilder.name("shipment-status-events")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
