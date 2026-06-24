package com.example.logisticstrackingservice.dto.request;

import com.example.logisticstrackingservice.enums.ConsignmentStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShipmentStatusRequest {

    @NotBlank
    private String consignmentNumber;

    @NotNull
    private ConsignmentStatus newStatus;

    private String remarks;
}
