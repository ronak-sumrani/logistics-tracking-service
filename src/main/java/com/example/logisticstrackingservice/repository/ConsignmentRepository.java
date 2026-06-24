package com.example.logisticstrackingservice.repository;

import com.example.logisticstrackingservice.entity.Consignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsignmentRepository extends JpaRepository<Consignment, Long> {
}
