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
public class SendGridEmailProvider implements NotificationProvider {

    private final String name = "SENDGRID";
    private final double cost = 0.01;
    private final int priority = 1;

    @Override
    public void sendSms(NotificationDto notification) throws ProviderException {
        throw new ProviderException("SENDGRID does not support sms");
    }

    @Override
    public void sendEmail(NotificationDto notification) throws ProviderException {
        try {
            log.info("[SENDGRID] SIMULATED EMAIL SEND -> {}", notification);
        } catch (Exception e) {
            throw new ProviderException("SendGrid email send failed", e);
        }
    }

    @Override
    public void sendPush(NotificationDto notification) throws ProviderException {
        throw new ProviderException("SENDGRID does not support push");
    }
}