package com.example.logisticstrackingservice.mapper;

import com.example.logisticstrackingservice.entity.Consignment;
import com.example.logisticstrackingservice.entity.NotificationHistory;
import com.example.logisticstrackingservice.enums.ConsignmentStatus;
import com.example.logisticstrackingservice.enums.NotificationStatus;
import com.example.logisticstrackingservice.kafka.dto.NotificationDto;
import org.springframework.stereotype.Component;

@Component
public class NotificationHistoryMapper {

    public NotificationHistory toEntity(NotificationDto dto, Consignment consignment,
                                        ConsignmentStatus consignmentStatus, String eventId) {
        NotificationHistory history = new NotificationHistory();
        history.setConsignment(consignment);
        history.setChannel(dto.getChannel());
        history.setMessage(dto.getMessage());
        history.setConsignmentStatus(consignmentStatus);
        history.setNotificationStatus(NotificationStatus.SENT);
        history.setEventId(eventId);
        return history;
    }
}