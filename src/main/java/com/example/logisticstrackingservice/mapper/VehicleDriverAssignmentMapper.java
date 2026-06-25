package com.example.logisticstrackingservice.mapper;

import com.example.logisticstrackingservice.dto.response.VehicleDriverAssignmentResponse;
import com.example.logisticstrackingservice.entity.VehicleDriverAssignment;
import org.springframework.stereotype.Component;

@Component
public class VehicleDriverAssignmentMapper {
    public VehicleDriverAssignmentResponse toResponse(VehicleDriverAssignment assignment) {
        VehicleDriverAssignmentResponse response = new VehicleDriverAssignmentResponse();
        response.setId(assignment.getId());
        response.setVehicleNumber(assignment.getVehicle().getVehicleNumber());
        response.setDriverName(assignment.getDriver().getName());
        response.setDriverMobile(assignment.getDriver().getMobile());
        response.setAssignedAt(assignment.getAssignedAt());
        return response;
    }
}
