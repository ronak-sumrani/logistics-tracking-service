package com.example.logisticstrackingservice.service;

import com.example.logisticstrackingservice.dto.request.AssignDriverRequest;
import com.example.logisticstrackingservice.dto.response.VehicleDriverAssignmentResponse;
import com.example.logisticstrackingservice.entity.Driver;
import com.example.logisticstrackingservice.entity.Vehicle;
import com.example.logisticstrackingservice.entity.VehicleDriverAssignment;
import com.example.logisticstrackingservice.mapper.VehicleDriverAssignmentMapper;
import com.example.logisticstrackingservice.repository.DriverRepository;
import com.example.logisticstrackingservice.repository.VehicleDriverAssignmentRepository;
import com.example.logisticstrackingservice.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehicleDriverAssignmentService {

    @Autowired
    private VehicleDriverAssignmentRepository vehicleDriverAssignmentRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private VehicleDriverAssignmentMapper vehicleDriverAssignmentMapper;

    @Transactional
    public VehicleDriverAssignmentResponse assignDriverToVehicle(AssignDriverRequest request) {
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found for id: " + request.getVehicleId()));

        Driver driver = driverRepository.findById(request.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found for id: " + request.getDriverId()));

        if (vehicleDriverAssignmentRepository.existsByVehicleAndActiveTrue(vehicle)) {
            throw new RuntimeException("Vehicle already has an active driver assigned");
        }

        VehicleDriverAssignment assignment = new VehicleDriverAssignment();
        assignment.setVehicle(vehicle);
        assignment.setDriver(driver);
        vehicleDriverAssignmentRepository.save(assignment);
        return vehicleDriverAssignmentMapper.toResponse(assignment);
    }

    @Transactional
    public void unassignDriver(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found for id: " + vehicleId));

        VehicleDriverAssignment assignment = vehicleDriverAssignmentRepository
                .findByVehicleAndActiveTrue(vehicle)
                .orElseThrow(() -> new RuntimeException("No active driver assignment found for vehicle: " + vehicleId));

        assignment.setActive(false);
        vehicleDriverAssignmentRepository.save(assignment);
    }
}