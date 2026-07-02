package com.example.logisticstrackingservice.controller;

import com.example.logisticstrackingservice.dto.request.AssignVehicleRequest;
import com.example.logisticstrackingservice.dto.request.CreateConsignmentRequest;
import com.example.logisticstrackingservice.dto.request.UpdateShipmentStatusRequest;
import com.example.logisticstrackingservice.dto.response.ConsignmentResponse;
import com.example.logisticstrackingservice.dto.response.ShipmentHistoryResponse;
import com.example.logisticstrackingservice.dto.response.ConsignmentStatusResponse;
import com.example.logisticstrackingservice.service.ConsignmentService;
import com.example.logisticstrackingservice.service.ShipmentHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/consignments")
public class ConsignmentController {

    private final ConsignmentService consignmentService;
    private final ShipmentHistoryService shipmentHistoryService;

    @PostMapping
    public ResponseEntity<ConsignmentResponse> createConsignment(@Valid @RequestBody CreateConsignmentRequest request) {
        ConsignmentResponse consignmentResponse = consignmentService.createConsignment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(consignmentResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsignmentResponse> getConsignmentById(@PathVariable Long id) {
        ConsignmentResponse response = consignmentService.getConsignmentById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{consignmentNumber}/tracking")
    public ResponseEntity<ConsignmentStatusResponse> getTrackingInfo(@PathVariable String consignmentNumber) {
        ConsignmentStatusResponse response = consignmentService.getTrackingInfo(consignmentNumber);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/assign-vehicle")
    public ResponseEntity<ConsignmentResponse> assignVehicle(@Valid @RequestBody AssignVehicleRequest request) {
        ConsignmentResponse response = consignmentService.assignVehicle(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/status-update")
    public ResponseEntity<ConsignmentResponse> updateStatus(@Valid @RequestBody UpdateShipmentStatusRequest request) {
        ConsignmentResponse response = consignmentService.updateShipmentStatus(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsignment(@PathVariable Long id) {
        consignmentService.deleteConsignmentById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<ShipmentHistoryResponse>> getShipmentHistory(@PathVariable Long id) {
        return ResponseEntity.ok(shipmentHistoryService.getHistoryByConsignmentId(id));
    }

    @GetMapping
    public ResponseEntity<List<ConsignmentResponse>> getAllConsignments() {
        return ResponseEntity.ok(consignmentService.getAllConsignments());
    }
}
