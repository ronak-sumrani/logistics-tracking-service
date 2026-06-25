package com.example.logisticstrackingservice.dto.response;

import com.example.logisticstrackingservice.enums.ConsignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentHistoryResponse {
    private Long id;
    private ConsignmentStatus oldStatus;
    private ConsignmentStatus newStatus;
    private String remarks;
    private LocalDateTime changedAt;
}