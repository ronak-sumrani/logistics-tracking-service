package com.example.logisticstrackingservice.kafka.producer;

import com.example.logisticstrackingservice.kafka.event.ShipmentStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ShipmentEventPublisherListener {

    private final ShipmentEventProducer shipmentEventProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onStatusChanged(ShipmentStatusChangedEvent event) {
        shipmentEventProducer.publish(event);
    }
}