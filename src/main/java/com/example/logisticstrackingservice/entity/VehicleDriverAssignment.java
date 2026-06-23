package com.example.logisticstrackingservice.entity;

import jakarta.persistence.*;
import lombok.Data;

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
}