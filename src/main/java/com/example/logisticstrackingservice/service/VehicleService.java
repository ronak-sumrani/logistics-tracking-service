package com.example.logisticstrackingservice.service;

import com.example.logisticstrackingservice.dto.request.CreateVehicleRequest;
import com.example.logisticstrackingservice.dto.response.VehicleResponse;
import com.example.logisticstrackingservice.entity.Vehicle;
import com.example.logisticstrackingservice.exception.VehicleAlreadyExistsException;
import com.example.logisticstrackingservice.exception.VehicleNotFoundException;
import com.example.logisticstrackingservice.mapper.VehicleMapper;
import com.example.logisticstrackingservice.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleMapper  vehicleMapper;

    public VehicleResponse findById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id).
                orElseThrow(() -> new VehicleNotFoundException("Vehicle not found for id: " + id));
        return vehicleMapper.toResponse(vehicle);
    }

    public List<VehicleResponse> findAll() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<VehicleResponse> vehicleResponses = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            vehicleResponses.add(vehicleMapper.toResponse(vehicle));
        }
        return vehicleResponses;
    }

    @Transactional
    public VehicleResponse createVehicle(CreateVehicleRequest request) {
        if (vehicleRepository.existsByVehicleNumber(request.getVehicleNumber())) {
            throw new VehicleAlreadyExistsException("Vehicle already exists with number: " + request.getVehicleNumber());
        }
        Vehicle vehicle = vehicleMapper.toEntity(request);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponse(savedVehicle);
    }
}
