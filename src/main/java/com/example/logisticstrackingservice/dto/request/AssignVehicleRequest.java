package com.example.logisticstrackingservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignVehicleRequest {
    @NotNull
    private Long consignmentId;

    @NotBlank
    private String vehicleNumber;
}
