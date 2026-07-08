package com.example.logisticstrackingservice.kafka.consumer;

import com.example.logisticstrackingservice.entity.Consignment;
import com.example.logisticstrackingservice.enums.AuditAction;
import com.example.logisticstrackingservice.kafka.event.ShipmentStatusChangedEvent;
import com.example.logisticstrackingservice.repository.AuditLogRepository;
import com.example.logisticstrackingservice.repository.ConsignmentRepository;
import com.example.logisticstrackingservice.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditConsumer {

    private final ConsignmentRepository consignmentRepository;
    private final AuditLogService auditLogService;
    private final AuditLogRepository auditLogRepository;

    @KafkaListener(topics = "shipment-status-events", groupId = "audit-service")
    public void consume(ShipmentStatusChangedEvent event) {
        if (event.getEventId() == null || event.getEventId().isBlank()) {
            log.error("[audit-consumer] received event without eventId for {}, skipping", event.getConsignmentNumber());
            return;
        }
        if (auditLogRepository.existsByEventId(event.getEventId())) {
            log.warn("[audit-consumer] duplicate event {} for {}, skipping", event.getEventId(), event.getConsignmentNumber());
            return;
        }
        log.info("[audit-consumer] logging STATUS_UPDATED for {}", event.getConsignmentNumber());

        Consignment consignment = consignmentRepository.findById(event.getConsignmentId())
                .orElse(null);
        if (consignment == null) {
            log.warn("[audit-consumer] consignment {} not found, skipping audit log", event.getConsignmentId());
            return;
        }
        auditLogService.log(consignment, AuditAction.STATUS_UPDATED, event.getEventId());
    }
}