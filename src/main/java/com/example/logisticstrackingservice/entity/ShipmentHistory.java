package com.example.logisticstrackingservice.entity;

import com.example.logisticstrackingservice.enums.ConsignmentStatus;
import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipment_history")
@Data
public class ShipmentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consignment_id", nullable = false)
    private Consignment consignment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsignmentStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsignmentStatus newStatus;

    private String remarks;

    @Column(nullable = false)
    private LocalDateTime changedAt;

    @PrePersist
    protected void onChange() {
        this.changedAt = LocalDateTime.now();
    }
}
