package com.example.logisticstrackingservice.kafka.producer;

import com.example.logisticstrackingservice.kafka.event.ShipmentStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShipmentEventProducer {

    private static final String TOPIC = "shipment-status-events";

    private final KafkaTemplate<String, ShipmentStatusChangedEvent> kafkaTemplate;

    public void publish(ShipmentStatusChangedEvent event) {
        kafkaTemplate.send(TOPIC, event.getConsignmentNumber(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish event for consignment {}: {}",
                                event.getConsignmentNumber(), ex.getMessage(), ex);
                    } else {
                        log.info("Published status-change event for consignment {} to partition {}",
                                event.getConsignmentNumber(),
                                result.getRecordMetadata().partition());
                    }
                });
    }
}