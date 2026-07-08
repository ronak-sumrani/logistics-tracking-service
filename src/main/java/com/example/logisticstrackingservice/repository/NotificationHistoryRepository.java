package com.example.logisticstrackingservice.repository;

import com.example.logisticstrackingservice.entity.NotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {
    boolean existsByEventId(String eventId);
}