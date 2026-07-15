package com.example.logisticstrackingservice.dto.request;

import com.example.logisticstrackingservice.enums.NotificationChannel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequest {
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
    private String mobile;

    private List<NotificationChannel> preferredChannels;
}
