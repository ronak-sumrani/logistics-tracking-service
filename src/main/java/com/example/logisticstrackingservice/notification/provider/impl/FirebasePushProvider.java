package com.example.logisticstrackingservice.notification.provider.impl;

import com.example.logisticstrackingservice.exception.ProviderException;
import com.example.logisticstrackingservice.kafka.dto.NotificationDto;
import com.example.logisticstrackingservice.notification.provider.NotificationProvider;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Component
public class FirebasePushProvider implements NotificationProvider {

    private final String name = "FIREBASE";
    private final double cost = 0.0;
    private final int priority = 1;

    @Override
    public void sendSms(NotificationDto notification) throws ProviderException {
        throw new ProviderException("FIREBASE does not support sms");
    }

    @Override
    public void sendEmail(NotificationDto notification) throws ProviderException {
        throw new ProviderException("FIREBASE does not support email");
    }

    @Override
    public void sendPush(NotificationDto notification) throws ProviderException {
        try {
            log.info("[FIREBASE] SIMULATED PUSH SEND -> {}", notification);
        } catch (Exception e) {
            throw new ProviderException("Firebase push send failed", e);
        }
    }
}