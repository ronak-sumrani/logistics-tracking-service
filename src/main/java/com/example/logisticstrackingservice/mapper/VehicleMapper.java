package com.example.logisticstrackingservice.mapper;

import com.example.logisticstrackingservice.dto.request.CreateVehicleRequest;
import com.example.logisticstrackingservice.dto.response.VehicleResponse;
import com.example.logisticstrackingservice.entity.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {
    public VehicleResponse toResponse(Vehicle vehicle) {
        VehicleResponse response = new VehicleResponse();
        response.setVehicleNumber(vehicle.getVehicleNumber());
        response.setVehicleType(vehicle.getVehicleType());
        response.setCreatedAt(vehicle.getCreatedAt());
        return response;
    }

    public Vehicle toEntity(CreateVehicleRequest request) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber(request.getVehicleNumber());
        vehicle.setVehicleType(request.getVehicleType());
        return vehicle;
    }
}
