package com.example.logisticstrackingservice.mapper;

import com.example.logisticstrackingservice.dto.response.ShipmentHistoryResponse;
import com.example.logisticstrackingservice.entity.ShipmentHistory;
import org.springframework.stereotype.Component;

@Component
public class ShipmentHistoryMapper {

    public ShipmentHistoryResponse toResponse(ShipmentHistory shipmentHistory) {
        ShipmentHistoryResponse response = new ShipmentHistoryResponse();
        response.setId(shipmentHistory.getId());
        response.setOldStatus(shipmentHistory.getOldStatus());
        response.setNewStatus(shipmentHistory.getNewStatus());
        response.setRemarks(shipmentHistory.getRemarks());
        response.setChangedAt(shipmentHistory.getChangedAt());
        return response;
    }
}