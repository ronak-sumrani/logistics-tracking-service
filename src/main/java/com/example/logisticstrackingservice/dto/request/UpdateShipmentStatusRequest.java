package com.example.logisticstrackingservice.dto.request;

import com.example.logisticstrackingservice.enums.ConsignmentStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShipmentStatusRequest {

    @NotNull
    private ConsignmentStatus newStatus;

    private String remarks;
}
