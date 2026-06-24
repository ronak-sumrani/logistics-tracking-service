package com.example.logisticstrackingservice.repository;

import com.example.logisticstrackingservice.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
