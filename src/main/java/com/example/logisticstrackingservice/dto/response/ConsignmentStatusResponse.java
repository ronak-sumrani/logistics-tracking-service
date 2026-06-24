package com.example.logisticstrackingservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsignmentStatusResponse {
    private Long id;
    private String consignmentNumber;
    private String status;
}
