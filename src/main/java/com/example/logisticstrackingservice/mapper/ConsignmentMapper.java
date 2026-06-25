package com.example.logisticstrackingservice.mapper;

import com.example.logisticstrackingservice.dto.request.CreateConsignmentRequest;
import com.example.logisticstrackingservice.dto.response.ConsignmentResponse;
import com.example.logisticstrackingservice.dto.response.VehicleResponse;
import com.example.logisticstrackingservice.entity.Consignment;
import com.example.logisticstrackingservice.entity.Customer;
import com.example.logisticstrackingservice.enums.ConsignmentStatus;
import org.springframework.stereotype.Component;

@Component
public class ConsignmentMapper {

    public ConsignmentResponse toResponse(Consignment consignment) {
        ConsignmentResponse response = new ConsignmentResponse();
        response.setId(consignment.getId());
        response.setConsignmentNumber(consignment.getConsignmentNumber());
        response.setStatus(consignment.getStatus());
        response.setOrigin(consignment.getOrigin());
        response.setDestination(consignment.getDestination());
        response.setSenderName(consignment.getSender().getName());
        response.setReceiverName(consignment.getReceiver().getName());
        response.setCreatedAt(consignment.getCreatedAt());
        response.setUpdatedAt(consignment.getUpdatedAt());

        if (consignment.getAssignedVehicle() != null) {
            VehicleResponse vehicleResponse = new VehicleResponse();
            vehicleResponse.setVehicleNumber(consignment.getAssignedVehicle().getVehicleNumber());
            vehicleResponse.setVehicleType(consignment.getAssignedVehicle().getVehicleType());
            vehicleResponse.setCreatedAt(consignment.getAssignedVehicle().getCreatedAt());
            response.setAssignedVehicle(vehicleResponse);
        }

        return response;
    }

    public Consignment toEntity(CreateConsignmentRequest request, Customer sender, Customer receiver, String consignmentNumber) {
        Consignment consignment = new Consignment();
        consignment.setConsignmentNumber(consignmentNumber);
        consignment.setSender(sender);
        consignment.setReceiver(receiver);
        consignment.setOrigin(request.getOrigin());
        consignment.setDestination(request.getDestination());
        consignment.setStatus(ConsignmentStatus.CREATED);
        return consignment;
    }
}