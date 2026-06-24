package com.example.logisticstrackingservice.dto.response;

import com.example.logisticstrackingservice.enums.ConsignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsignmentStatusResponse {
    private Long id;
    private String consignmentNumber;
    private ConsignmentStatus status;
}
