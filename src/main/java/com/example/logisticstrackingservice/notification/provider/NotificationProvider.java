package com.example.logisticstrackingservice.notification.provider;

import com.example.logisticstrackingservice.exception.ProviderException;
import com.example.logisticstrackingservice.kafka.dto.NotificationDto;

public interface NotificationProvider {

    String getName();
    double getCost();
    int getPriority();

    void sendSms(NotificationDto notification) throws ProviderException;
    void sendEmail(NotificationDto notification) throws ProviderException;
    void sendPush(NotificationDto notification) throws ProviderException;
}