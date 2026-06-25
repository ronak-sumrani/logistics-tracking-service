package com.example.logisticstrackingservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDriverAssignmentResponse {
    private Long id;
    private String vehicleNumber;
    private String driverName;
    private String driverMobile;
    private LocalDateTime assignedAt;
}
