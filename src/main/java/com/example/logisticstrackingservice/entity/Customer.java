package com.example.logisticstrackingservice.entity;

import com.example.logisticstrackingservice.enums.NotificationChannel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "customers")
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @Column(nullable = false)
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
    private String mobile;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "preferred_channels")
    private List<NotificationChannel> preferredChannels;

    @PrePersist
    protected void onCreate() {
        if (preferredChannels == null || preferredChannels.isEmpty()) {
            preferredChannels = List.of(NotificationChannel.SMS);
        }
    }
}