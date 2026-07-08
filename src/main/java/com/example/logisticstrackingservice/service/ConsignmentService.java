package com.example.logisticstrackingservice.service;

import com.example.logisticstrackingservice.annotation.Auditable;
import com.example.logisticstrackingservice.dto.request.AssignVehicleRequest;
import com.example.logisticstrackingservice.dto.request.CreateConsignmentRequest;
import com.example.logisticstrackingservice.dto.request.UpdateShipmentStatusRequest;
import com.example.logisticstrackingservice.dto.response.ConsignmentResponse;
import com.example.logisticstrackingservice.dto.response.ConsignmentStatusResponse;
import com.example.logisticstrackingservice.entity.Consignment;
import com.example.logisticstrackingservice.entity.Customer;
import com.example.logisticstrackingservice.entity.OutboxEvent;
import com.example.logisticstrackingservice.entity.Vehicle;
import com.example.logisticstrackingservice.enums.AuditAction;
import com.example.logisticstrackingservice.enums.ConsignmentStatus;
import com.example.logisticstrackingservice.exception.*;
import com.example.logisticstrackingservice.kafka.event.ShipmentStatusChangedEvent;
import com.example.logisticstrackingservice.mapper.ConsignmentMapper;
import com.example.logisticstrackingservice.mapper.ShipmentEventMapper;
import com.example.logisticstrackingservice.repository.ConsignmentRepository;
import com.example.logisticstrackingservice.repository.OutboxEventRepository;
import com.example.logisticstrackingservice.repository.VehicleDriverAssignmentRepository;
import com.example.logisticstrackingservice.repository.VehicleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ConsignmentService {

    private final ConsignmentRepository consignmentRepository;
    private final ConsignmentMapper consignmentMapper;
    private final CustomerService customerService;
    private final VehicleRepository vehicleRepository;
    private final VehicleDriverAssignmentRepository vehicleDriverAssignmentRepository;
    private final ShipmentEventMapper shipmentEventMapper;
    private final ObjectMapper objectMapper;
    private final OutboxEventRepository outboxEventRepository;

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

    @Auditable(action = AuditAction.SHIPMENT_CREATED)
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
    public ConsignmentResponse updateShipmentStatus(Long consignmentId, UpdateShipmentStatusRequest request) {
        Consignment consignment = consignmentRepository.findById(consignmentId)
                .orElseThrow(() -> new ConsignmentNotFoundException("Consignment not found for id: " + consignmentId));
        if (consignment.getAssignedVehicle() == null) {
            throw new VehicleNotAssignedException("Cannot update status - no vehicle assigned to consignment: " + consignment.getConsignmentNumber());
        }
        ConsignmentStatus oldStatus = consignment.getStatus();
        if (!oldStatus.canTransitionTo(request.getNewStatus())) {
            throw new InvalidStatusTransitionException(
                    "Cannot transition from " + oldStatus + " to " + request.getNewStatus()
            );
        }
        consignment.setStatus(request.getNewStatus());
        consignmentRepository.save(consignment);

        ShipmentStatusChangedEvent event = shipmentEventMapper.toEvent(consignment, oldStatus, request.getNewStatus(), request.getRemarks());

        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setEventId(event.getEventId());
        outboxEvent.setTopic("shipment-status-events");
        outboxEvent.setAggregateKey(event.getConsignmentNumber());
        try {
            outboxEvent.setPayload(objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
        outboxEventRepository.save(outboxEvent);
        return consignmentMapper.toResponse(consignment);
    }

    @Transactional
    public void deleteConsignmentById(Long id) {
        Consignment consignment = consignmentRepository.findById(id)
                .orElseThrow(() -> new ConsignmentNotFoundException("Consignment not found for id: " + id));
        consignment.setActive(false);
        consignmentRepository.save(consignment);
    }

    @Auditable(action = AuditAction.VEHICLE_ASSIGNED)
    @Transactional
    public ConsignmentResponse assignVehicle(Long consignmentId, AssignVehicleRequest request) {
        Consignment consignment = consignmentRepository.findById(consignmentId)
                .orElseThrow(() -> new ConsignmentNotFoundException("Consignment not found for id: " + consignmentId));
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
