package com.example.logisticstrackingservice.aspect;

import com.example.logisticstrackingservice.annotation.Auditable;

import com.example.logisticstrackingservice.dto.response.ConsignmentResponse;
import com.example.logisticstrackingservice.entity.Consignment;
import com.example.logisticstrackingservice.enums.AuditAction;
import com.example.logisticstrackingservice.repository.ConsignmentRepository;
import com.example.logisticstrackingservice.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@RequiredArgsConstructor
@Aspect
@Component
public class AuditAspect {

    private final AuditLogService auditLogService;
    private final ConsignmentRepository consignmentRepository;

    @AfterReturning(
            pointcut = "@annotation(com.example.logisticstrackingservice.annotation.Auditable)",
            returning = "result"
    )
    public void audit(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Auditable auditable = method.getAnnotation(Auditable.class);
        AuditAction action = auditable.action();

        Consignment consignment = resolveConsignment(joinPoint, result);
        if (consignment != null) {
            auditLogService.log(consignment, action);
        }
    }

    private Consignment resolveConsignment(JoinPoint joinPoint, Object result) {
        // Get consignment from return value
        if (result instanceof ConsignmentResponse response) {
            return consignmentRepository.findById(response.getId()).orElse(null);
        }
        return null;
    }
}