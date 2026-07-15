package com.example.logisticstrackingservice.kafka.dto;

import com.example.logisticstrackingservice.enums.NotificationChannel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class NotificationDto {
    private String consignmentNumber;
    private NotificationChannel channel;
    private String message;
}