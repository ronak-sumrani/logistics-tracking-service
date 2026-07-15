package com.example.logisticstrackingservice.dto.response;

import com.example.logisticstrackingservice.enums.NotificationChannel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    private Long id;
    private String name;
    private String mobile;
    private List<NotificationChannel> preferredChannels;
}