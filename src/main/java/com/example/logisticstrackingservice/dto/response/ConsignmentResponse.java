package com.example.logisticstrackingservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsignmentResponse {
    private Long id;
    private String consignmentNumber;
    private String status;
    private String origin;
    private String destination;
    private String senderName;
    private String receiverName;
    private VehicleResponse assignedVehicle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
