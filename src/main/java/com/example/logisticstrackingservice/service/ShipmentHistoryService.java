package com.example.logisticstrackingservice.service;

import com.example.logisticstrackingservice.entity.Consignment;
import com.example.logisticstrackingservice.entity.ShipmentHistory;
import com.example.logisticstrackingservice.enums.ConsignmentStatus;
import com.example.logisticstrackingservice.repository.ShipmentHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShipmentHistoryService {

    @Autowired
    private ShipmentHistoryRepository shipmentHistoryRepository;

    @Transactional
    public void recordStatusChange(Consignment consignment, ConsignmentStatus oldStatus, ConsignmentStatus newStatus, String remarks) {
        ShipmentHistory shipmentHistory = new ShipmentHistory();
        shipmentHistory.setConsignment(consignment);
        shipmentHistory.setOldStatus(oldStatus);
        shipmentHistory.setNewStatus(newStatus);
        shipmentHistory.setRemarks(remarks);
        shipmentHistoryRepository.save(shipmentHistory);
    }
}
