package com.example.logisticstrackingservice.kafka.consumer;

import com.example.logisticstrackingservice.entity.Consignment;
import com.example.logisticstrackingservice.enums.NotificationChannel;
import com.example.logisticstrackingservice.enums.NotificationStatus;
import com.example.logisticstrackingservice.exception.AllProvidersFailedException;
import com.example.logisticstrackingservice.kafka.dto.NotificationDto;
import com.example.logisticstrackingservice.kafka.event.ShipmentStatusChangedEvent;
import com.example.logisticstrackingservice.mapper.NotificationHistoryMapper;
import com.example.logisticstrackingservice.notification.provider.NotificationProviderSelector;
import com.example.logisticstrackingservice.repository.ConsignmentRepository;
import com.example.logisticstrackingservice.repository.NotificationHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationHistoryRepository notificationHistoryRepository;
    private final ConsignmentRepository consignmentRepository;
    private final NotificationHistoryMapper notificationHistoryMapper;
    private final NotificationProviderSelector notificationProviderSelector;

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
        Consignment consignment = consignmentRepository.findByIdWithReceiver(event.getConsignmentId()).orElse(null);
        if (consignment == null) {
            log.warn("[notification-consumer] consignment {} not found, skipping notification",
                    event.getConsignmentId());
            acknowledgment.acknowledge();
            return;
        }

        List<NotificationChannel> preferredChannels = consignment.getReceiver().getPreferredChannels();

        NotificationDto notification = NotificationDto.builder()
                .consignmentNumber(event.getConsignmentNumber())
                .channel(preferredChannels.isEmpty() ? NotificationChannel.SMS : preferredChannels.get(0))
                .message("Shipment " + event.getConsignmentNumber()
                        + " status changed from " + event.getOldStatus()
                        + " to " + event.getNewStatus())
                .build();

        NotificationStatus resultStatus;
        try {
            NotificationChannel usedChannel = notificationProviderSelector.send(notification, preferredChannels);
            notification.setChannel(usedChannel);
            resultStatus = NotificationStatus.SENT;
            log.info("[notification-consumer] notification sent for {} via {}", event.getConsignmentNumber(), usedChannel);
        } catch (AllProvidersFailedException e) {
            resultStatus = NotificationStatus.FAILED;
            log.error("[notification-consumer] all providers/channels failed for {}: {}",
                    event.getConsignmentNumber(), e.getMessage());
        }

        notificationHistoryRepository.save(
                notificationHistoryMapper.toEntity(notification, consignment, event.getNewStatus(), event.getEventId(), resultStatus)
        );
        acknowledgment.acknowledge();
    }
}