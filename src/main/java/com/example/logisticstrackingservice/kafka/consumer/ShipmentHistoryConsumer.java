package com.example.logisticstrackingservice.kafka.consumer;

import com.example.logisticstrackingservice.entity.Consignment;
import com.example.logisticstrackingservice.kafka.event.ShipmentStatusChangedEvent;
import com.example.logisticstrackingservice.repository.ConsignmentRepository;
import com.example.logisticstrackingservice.repository.ShipmentHistoryRepository;
import com.example.logisticstrackingservice.service.ShipmentHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShipmentHistoryConsumer {

    private final ConsignmentRepository consignmentRepository;
    private final ShipmentHistoryRepository shipmentHistoryRepository;
    private final ShipmentHistoryService shipmentHistoryService;

    @KafkaListener(topics = "shipment-status-events", groupId = "shipment-history-service")
    public void consume(ShipmentStatusChangedEvent event) {
        if (shipmentHistoryRepository.existsByEventId(event.getEventId())) {
            log.warn("[history-consumer] duplicate event {} for {}, skipping", event.getEventId(), event.getConsignmentNumber());
            return;
        }

        log.info("[history-consumer] processing status change for {}", event.getConsignmentNumber());

        Consignment consignment = consignmentRepository.findById(event.getConsignmentId())
                .orElse(null);
        if (consignment == null) {
            log.warn("[history-consumer] consignment {} not found, skipping history record", event.getConsignmentId());
            return;
        }

        shipmentHistoryService.recordStatusChange(
                consignment,
                event.getOldStatus(),
                event.getNewStatus(),
                event.getRemarks(),
                event.getEventId()
        );
    }
}