package com.example.logisticstrackingservice.service;

import com.example.logisticstrackingservice.dto.request.AssignDriverRequest;
import com.example.logisticstrackingservice.dto.response.VehicleDriverAssignmentResponse;
import com.example.logisticstrackingservice.entity.Driver;
import com.example.logisticstrackingservice.entity.Vehicle;
import com.example.logisticstrackingservice.entity.VehicleDriverAssignment;
import com.example.logisticstrackingservice.exception.ActiveDriverAssignmentExistsException;
import com.example.logisticstrackingservice.exception.DriverNotFoundException;
import com.example.logisticstrackingservice.exception.NoActiveDriverAssignmentException;
import com.example.logisticstrackingservice.exception.VehicleNotFoundException;
import com.example.logisticstrackingservice.mapper.VehicleDriverAssignmentMapper;
import com.example.logisticstrackingservice.repository.DriverRepository;
import com.example.logisticstrackingservice.repository.VehicleDriverAssignmentRepository;
import com.example.logisticstrackingservice.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class VehicleDriverAssignmentService {

    private final VehicleDriverAssignmentRepository vehicleDriverAssignmentRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final VehicleDriverAssignmentMapper vehicleDriverAssignmentMapper;

    @Transactional
    public VehicleDriverAssignmentResponse assignDriverToVehicle(AssignDriverRequest request) {
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found for id: " + request.getVehicleId()));

        Driver driver = driverRepository.findById(request.getDriverId())
                .orElseThrow(() -> new DriverNotFoundException("Driver not found for id: " + request.getDriverId()));

        if (vehicleDriverAssignmentRepository.existsByDriverAndActiveTrue(driver)) {
            throw new ActiveDriverAssignmentExistsException("Driver is already actively assigned to another vehicle");
        }

        if (vehicleDriverAssignmentRepository.existsByVehicleAndDriverAndActiveTrue(vehicle, driver)) {
            throw new ActiveDriverAssignmentExistsException("Driver is already assigned to this vehicle");
        }

        if (vehicleDriverAssignmentRepository.existsByVehicleAndActiveTrue(vehicle)) {
            throw new ActiveDriverAssignmentExistsException("Vehicle already has an active driver assigned");
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
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found for id: " + vehicleId));

        VehicleDriverAssignment assignment = vehicleDriverAssignmentRepository
                .findByVehicleAndActiveTrue(vehicle)
                .orElseThrow(() -> new NoActiveDriverAssignmentException("No active driver assignment found for vehicle: " + vehicleId));

        assignment.setActive(false);
        vehicleDriverAssignmentRepository.save(assignment);
    }
}