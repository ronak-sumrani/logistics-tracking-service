package com.example.logisticstrackingservice.service;

import com.example.logisticstrackingservice.entity.AuditLog;
import com.example.logisticstrackingservice.entity.Consignment;
import com.example.logisticstrackingservice.enums.AuditAction;
import com.example.logisticstrackingservice.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    // used by AuditAspect -- synchronous path, no dedup needed
    public void log(Consignment consignment, AuditAction action) {
        log(consignment, action, null);
    }

    // used by AuditConsumer -- Kafka path, dedup via eventId
    public void log(Consignment consignment, AuditAction action, String eventId) {
        AuditLog auditLog = new AuditLog();
        auditLog.setConsignment(consignment);
        auditLog.setAction(action);
        auditLog.setPerformedBy("SYSTEM");
        auditLog.setEventId(eventId);
        auditLogRepository.save(auditLog);
    }
}