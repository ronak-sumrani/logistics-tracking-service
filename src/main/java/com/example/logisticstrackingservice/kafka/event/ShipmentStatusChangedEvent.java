package com.example.logisticstrackingservice.kafka.event;

import com.example.logisticstrackingservice.enums.ConsignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentStatusChangedEvent {
    private Long consignmentId;
    private String consignmentNumber;
    private ConsignmentStatus oldStatus;
    private ConsignmentStatus newStatus;
    private String remarks;
    private LocalDateTime timestamp;
}