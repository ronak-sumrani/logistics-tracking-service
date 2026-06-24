package com.example.logisticstrackingservice.repository;

import com.example.logisticstrackingservice.entity.VehicleDriverAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleDriverAssignmentRepository extends JpaRepository<VehicleDriverAssignment, Integer> {
}
