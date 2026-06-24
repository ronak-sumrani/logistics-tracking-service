package com.example.logisticstrackingservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_driver_assignments")
@Data
public class VehicleDriverAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @Column(nullable = false)
    private boolean isActive;

    @Column
    private LocalDateTime assignedAt;

    @Column(nullable = false)
    private LocalDateTime unassignedAt;

    @PrePersist
    protected void onAssign() {
        this.assignedAt = LocalDateTime.now();
        this.unassignedAt = null;
    }

    @PreUpdate
    protected void onUnassign() {
        this.unassignedAt = LocalDateTime.now();
        this.assignedAt = null;
    }
}