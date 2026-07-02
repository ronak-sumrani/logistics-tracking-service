package com.example.logisticstrackingservice.service;

import com.example.logisticstrackingservice.dto.response.ShipmentHistoryResponse;
import com.example.logisticstrackingservice.entity.Consignment;
import com.example.logisticstrackingservice.entity.ShipmentHistory;
import com.example.logisticstrackingservice.enums.ConsignmentStatus;
import com.example.logisticstrackingservice.mapper.ShipmentHistoryMapper;
import com.example.logisticstrackingservice.repository.ShipmentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ShipmentHistoryService {

    private final ShipmentHistoryRepository shipmentHistoryRepository;
    private final ShipmentHistoryMapper shipmentHistoryMapper;

    @Transactional
    public void recordStatusChange(Consignment consignment, ConsignmentStatus oldStatus, ConsignmentStatus newStatus, String remarks) {
        ShipmentHistory shipmentHistory = new ShipmentHistory();
        shipmentHistory.setConsignment(consignment);
        shipmentHistory.setOldStatus(oldStatus);
        shipmentHistory.setNewStatus(newStatus);
        shipmentHistory.setRemarks(remarks);
        shipmentHistoryRepository.save(shipmentHistory);
    }

    public List<ShipmentHistoryResponse> getHistoryByConsignmentId(Long consignmentId) {
        return shipmentHistoryRepository.findByConsignmentIdOrderByChangedAtAsc(consignmentId)
                .stream()
                .map(shipmentHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }
}
