package com.example.logisticstrackingservice.kafka.dto;

import com.example.logisticstrackingservice.enums.NotificationChannel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NotificationDto {
    private String consignmentNumber;
    private NotificationChannel channel;
    private String message;
}