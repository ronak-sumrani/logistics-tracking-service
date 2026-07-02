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

    public void log(Consignment consignment, AuditAction action) {
        AuditLog auditLog = new AuditLog();
        auditLog.setConsignment(consignment);
        auditLog.setAction(action);
        auditLog.setPerformedBy("SYSTEM");
        auditLogRepository.save(auditLog);
    }
}