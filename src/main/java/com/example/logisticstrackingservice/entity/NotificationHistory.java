package com.example.logisticstrackingservice.entity;

import com.example.logisticstrackingservice.enums.ConsignmentStatus;
import com.example.logisticstrackingservice.enums.NotificationChannel;
import com.example.logisticstrackingservice.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_history")
@Data
public class NotificationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consignment_id", nullable = false)
    private Consignment consignment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationChannel channel;

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsignmentStatus consignmentStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus notificationStatus;

    @Column(name = "event_id", unique = true)
    private String eventId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}


// notification log entity
