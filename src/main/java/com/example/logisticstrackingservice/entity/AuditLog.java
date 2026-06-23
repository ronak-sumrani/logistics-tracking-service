package com.example.logisticstrackingservice.entity;

import com.example.logisticstrackingservice.enums.AuditAction;
import jakarta.persistence.*;
import jakarta.persistence.GenerationType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consignment_id", nullable = false)
    private Consignment consignment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditAction action;

    @Column(nullable = false)
    private String performedBy;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime performedAt;
}
