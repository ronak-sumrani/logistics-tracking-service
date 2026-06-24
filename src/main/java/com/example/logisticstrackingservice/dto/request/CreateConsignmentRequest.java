package com.example.logisticstrackingservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateConsignmentRequest {
    @NotBlank
    @Size(min = 2, max = 100)
    private String senderName;

    @NotBlank
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid sender mobile number")
    private String senderMobile;

    @NotBlank
    @Size(min = 2, max = 100)
    private String receiverName;

    @NotBlank
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid receiver mobile number")
    private String receiverMobile;

    @NotBlank
    private String origin;

    @NotBlank
    private String destination;
}
