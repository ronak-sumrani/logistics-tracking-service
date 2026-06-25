package com.example.logisticstrackingservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignDriverRequest {
    @NotNull
    private Long vehicleId;
    @NotNull
    private Long driverId;
}
