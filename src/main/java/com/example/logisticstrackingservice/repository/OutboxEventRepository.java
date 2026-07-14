package com.example.logisticstrackingservice.repository;

import com.example.logisticstrackingservice.entity.OutboxEvent;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<OutboxEvent> findTop50ByPublishedFalseOrderByCreatedAtAsc();
    // Top50 is the batch size — each scheduled run processes at most 50 unpublished rows, oldest first.
    // If you have fewer than 50 waiting, you just get however many exist.
    Boolean existsByEventId(String eventId);
}