package com.example.logisticstrackingservice.kafka.consumer;

import com.example.logisticstrackingservice.entity.Consignment;
import com.example.logisticstrackingservice.enums.NotificationChannel;
import com.example.logisticstrackingservice.kafka.dto.NotificationDto;
import com.example.logisticstrackingservice.kafka.event.ShipmentStatusChangedEvent;
import com.example.logisticstrackingservice.mapper.NotificationHistoryMapper;
import com.example.logisticstrackingservice.repository.ConsignmentRepository;
import com.example.logisticstrackingservice.repository.NotificationHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationHistoryRepository notificationHistoryRepository;
    private final ConsignmentRepository consignmentRepository;
    private final NotificationHistoryMapper notificationHistoryMapper;

    @KafkaListener(topics = "shipment-status-events", groupId = "notification-service")
    public void consume(ShipmentStatusChangedEvent event, Acknowledgment acknowledgment) {
        if (event.getEventId() == null || event.getEventId().isBlank()) {
            log.error("[notification-consumer] received event without eventId for {}, skipping", event.getConsignmentNumber());
            acknowledgment.acknowledge();
            return;
        }
        if (notificationHistoryRepository.existsByEventId(event.getEventId())) {
            log.warn("[notification-consumer] duplicate event {} for {}, skipping",
                    event.getEventId(), event.getConsignmentNumber());
            acknowledgment.acknowledge();
            return;
        }
        Consignment consignment = consignmentRepository.findById(event.getConsignmentId())
                .orElse(null);
        if (consignment == null) {
            log.warn("[notification-consumer] consignment {} not found, skipping notification",
                    event.getConsignmentId());
            acknowledgment.acknowledge();
            return;
        }

        NotificationDto notification = NotificationDto.builder()
                .consignmentNumber(event.getConsignmentNumber())
                .channel(NotificationChannel.SMS)
                .message("Shipment " + event.getConsignmentNumber()
                        + " status changed from " + event.getOldStatus()
                        + " to " + event.getNewStatus())
                .build();

        log.info("[notification-consumer] SIMULATED SEND -> {}", notification);

        notificationHistoryRepository.save(
                notificationHistoryMapper.toEntity(notification, consignment, event.getNewStatus(), event.getEventId())
        );
        acknowledgment.acknowledge();
    }
}