package com.example.logisticstrackingservice.mapper;

import com.example.logisticstrackingservice.entity.Consignment;
import com.example.logisticstrackingservice.enums.ConsignmentStatus;
import com.example.logisticstrackingservice.kafka.event.ShipmentStatusChangedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ShipmentEventMapper {

    public ShipmentStatusChangedEvent toEvent(Consignment consignment, ConsignmentStatus oldStatus,
                                              ConsignmentStatus newStatus, String remarks) {
        return ShipmentStatusChangedEvent.builder()
                .consignmentId(consignment.getId())
                .consignmentNumber(consignment.getConsignmentNumber())
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .remarks(remarks)
                .timestamp(LocalDateTime.now())
                .build();
    }
}