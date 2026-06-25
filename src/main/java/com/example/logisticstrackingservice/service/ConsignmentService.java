package com.example.logisticstrackingservice.service;

import com.example.logisticstrackingservice.dto.request.AssignVehicleRequest;
import com.example.logisticstrackingservice.dto.request.CreateConsignmentRequest;
import com.example.logisticstrackingservice.dto.request.UpdateShipmentStatusRequest;
import com.example.logisticstrackingservice.dto.response.ConsignmentResponse;
import com.example.logisticstrackingservice.dto.response.ConsignmentStatusResponse;
import com.example.logisticstrackingservice.entity.Consignment;
import com.example.logisticstrackingservice.entity.Customer;
import com.example.logisticstrackingservice.entity.Vehicle;
import com.example.logisticstrackingservice.enums.ConsignmentStatus;
import com.example.logisticstrackingservice.exception.*;
import com.example.logisticstrackingservice.mapper.ConsignmentMapper;
import com.example.logisticstrackingservice.repository.ConsignmentRepository;
import com.example.logisticstrackingservice.repository.VehicleDriverAssignmentRepository;
import com.example.logisticstrackingservice.repository.VehicleRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class ConsignmentService {

    @Autowired
    private ConsignmentRepository consignmentRepository;

    @Autowired
    private ConsignmentMapper consignmentMapper;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShipmentHistoryService shipmentHistoryService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleDriverAssignmentRepository vehicleDriverAssignmentRepository;

    public ConsignmentResponse getConsignmentById(Long id) {
        Consignment consignment = consignmentRepository.findById(id).
                orElseThrow(() -> new ConsignmentNotFoundException("Consignment not found for id: " + id));
        return consignmentMapper.toResponse(consignment);
    }

    public List<ConsignmentResponse> getAllConsignments() {
        List<Consignment> consignments = consignmentRepository.findAll();
        List<ConsignmentResponse> consignmentResponses = new ArrayList<>();
        for (Consignment consignment : consignments) {
            consignmentResponses.add(consignmentMapper.toResponse(consignment));
        }
        return consignmentResponses;
    }

    @Transactional
    public ConsignmentResponse createConsignment(CreateConsignmentRequest request) {
        Customer sender = customerService.findOrCreate(request.getSenderName(), request.getSenderMobile());
        Customer receiver = customerService.findOrCreate(request.getReceiverName(), request.getReceiverMobile());
        String consignmentNumber = "CN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Consignment consignment = consignmentMapper.toEntity(request, sender, receiver, consignmentNumber);
        Consignment saved = consignmentRepository.save(consignment);
        return consignmentMapper.toResponse(saved);
    }

    @Transactional
    public ConsignmentResponse updateShipmentStatus(UpdateShipmentStatusRequest request) {
        Consignment consignment = consignmentRepository.findByConsignmentNumber(request.getConsignmentNumber())
                .orElseThrow(() -> new ConsignmentNotFoundException("Consignment not found for number: " + request.getConsignmentNumber()));
        if (consignment.getAssignedVehicle() == null) {
            throw new VehicleNotAssignedException("Cannot update status - no vehicle assigned to consignment: " + request.getConsignmentNumber());
        }
        ConsignmentStatus oldStatus = consignment.getStatus();
        if (!oldStatus.canTransitionTo(request.getNewStatus())) {
            throw new InvalidStatusTransitionException(
                    "Cannot transition from " + oldStatus + " to " + request.getNewStatus()
            );
        }
        shipmentHistoryService.recordStatusChange(consignment, oldStatus, request.getNewStatus(), request.getRemarks());
        consignment.setStatus(request.getNewStatus());
        consignmentRepository.save(consignment);
        return consignmentMapper.toResponse(consignment);
    }

    @Transactional
    public void deleteConsignmentById(Long id) {
        Consignment consignment = consignmentRepository.findById(id)
                .orElseThrow(() -> new ConsignmentNotFoundException("Consignment not found for id: " + id));
        consignment.setActive(false);
        consignmentRepository.save(consignment);
    }

    @Transactional
    public ConsignmentResponse assignVehicle(AssignVehicleRequest request) {
        Consignment consignment = consignmentRepository.findById(request.getConsignmentId())
                .orElseThrow(() -> new ConsignmentNotFoundException("Consignment not found for id: " + request.getConsignmentId()));

        if (consignment.getAssignedVehicle() != null) {
            throw new RuntimeException("Consignment already has a vehicle assigned");
        }
        Vehicle vehicle = vehicleRepository.findByVehicleNumber(request.getVehicleNumber())
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found for vehicle number: " + request.getVehicleNumber()));

        if (!vehicleDriverAssignmentRepository.existsByVehicleAndActiveTrue(vehicle)) {
            throw new NoActiveDriverAssignmentException("Vehicle has no active driver assigned");
        }
        consignment.setAssignedVehicle(vehicle);
        consignmentRepository.save(consignment);
        return consignmentMapper.toResponse(consignment);
    }
    // what if vehicle is already assigned? give error or re-assign?

    public ConsignmentStatusResponse getTrackingInfo(String consignmentNumber) {
        Consignment consignment = consignmentRepository.findByConsignmentNumber(consignmentNumber)
                .orElseThrow(() -> new ConsignmentNotFoundException("Consignment not found for number: " + consignmentNumber));
        ConsignmentStatusResponse response = new ConsignmentStatusResponse();
        response.setId(consignment.getId());
        response.setConsignmentNumber(consignment.getConsignmentNumber());
        response.setStatus(consignment.getStatus());
        return response;
    }

}
