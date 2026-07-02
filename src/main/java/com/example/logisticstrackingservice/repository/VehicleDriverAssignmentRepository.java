package com.example.logisticstrackingservice.repository;

import com.example.logisticstrackingservice.entity.Driver;
import com.example.logisticstrackingservice.entity.Vehicle;
import com.example.logisticstrackingservice.entity.VehicleDriverAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleDriverAssignmentRepository extends JpaRepository<VehicleDriverAssignment, Long> {
    boolean existsByVehicleAndActiveTrue(Vehicle vehicle);
    Optional<VehicleDriverAssignment> findByVehicleAndActiveTrue(Vehicle vehicle);
    boolean existsByVehicleAndDriverAndActiveTrue(Vehicle vehicle, Driver driver);
    boolean existsByDriverAndActiveTrue(Driver driver);
}
