package com.example.logisticstrackingservice.repository;


import com.example.logisticstrackingservice.entity.ShipmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentHistoryRepository extends JpaRepository<ShipmentHistory, Integer> {
}
