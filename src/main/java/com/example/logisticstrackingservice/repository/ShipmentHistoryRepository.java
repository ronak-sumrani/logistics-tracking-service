package com.example.logisticstrackingservice.repository;


import com.example.logisticstrackingservice.entity.ShipmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShipmentHistoryRepository extends JpaRepository<ShipmentHistory, Long> {
    List<ShipmentHistory> findByConsignmentIdOrderByChangedAtAsc(Long consignmentId);
}
