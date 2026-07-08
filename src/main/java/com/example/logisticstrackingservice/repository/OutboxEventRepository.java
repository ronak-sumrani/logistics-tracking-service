package com.example.logisticstrackingservice.repository;

import com.example.logisticstrackingservice.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findTop50ByPublishedFalseOrderByCreatedAtAsc();
    // Top50 is the batch size — each scheduled run processes at most 50 unpublished rows, oldest first.
    // If you have fewer than 50 waiting, you just get however many exist.
}