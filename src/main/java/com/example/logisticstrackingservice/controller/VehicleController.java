package com.example.logisticstrackingservice.controller;

import com.example.logisticstrackingservice.dto.request.AssignDriverRequest;
import com.example.logisticstrackingservice.dto.request.CreateVehicleRequest;
import com.example.logisticstrackingservice.dto.response.VehicleDriverAssignmentResponse;
import com.example.logisticstrackingservice.dto.response.VehicleResponse;
import com.example.logisticstrackingservice.service.VehicleDriverAssignmentService;
import com.example.logisticstrackingservice.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehicleDriverAssignmentService vehicleDriverAssignmentService;

    @PostMapping
    public ResponseEntity<VehicleResponse> createVehicle(@Valid @RequestBody CreateVehicleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.createVehicle(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponse>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.findAll());
    }

    @PostMapping("/assign-driver")
    public ResponseEntity<VehicleDriverAssignmentResponse> assignDriver(@Valid @RequestBody AssignDriverRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleDriverAssignmentService.assignDriverToVehicle(request));
    }

    @PutMapping("/{vehicleId}/unassign-driver")
    public ResponseEntity<Void> unassignDriver(@PathVariable Long vehicleId) {
        vehicleDriverAssignmentService.unassignDriver(vehicleId);
        return ResponseEntity.noContent().build();
    }
}
