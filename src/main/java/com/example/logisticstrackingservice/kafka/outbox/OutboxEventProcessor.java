package com.example.logisticstrackingservice.kafka.outbox;

import com.example.logisticstrackingservice.entity.OutboxEvent;
import com.example.logisticstrackingservice.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxEventProcessor {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> stringKafkaTemplate;

    // This creates an isolated Read-Write transaction for EVERY individual event
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processAndPublish(Long id) {

        // 1. Fetch the full entity (safely reads the OID and gets the row lock)
        OutboxEvent outboxEvent = outboxEventRepository.findByIdAndPublishedFalseWithLock(id).orElse(null);

        // Safety check in case another node processed it a millisecond ago
        if (outboxEvent == null || outboxEvent.isPublished()) {
            return;
        }

        // 2. Publish to Kafka
        try {
            stringKafkaTemplate.send(outboxEvent.getTopic(), outboxEvent.getAggregateKey(), outboxEvent.getPayload())
                    .get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            // Throwing a RuntimeException forces ONLY this specific event's database transaction to roll back
            throw new RuntimeException("Kafka publish failed for event " + outboxEvent.getEventId(), e);
        }

        // 3. Mark as published
        outboxEvent.setPublished(true);
        outboxEvent.setPublishedAt(LocalDateTime.now());
        outboxEventRepository.save(outboxEvent);

        log.info("[outbox-poller] successfully published event {} for {}", outboxEvent.getEventId(), outboxEvent.getAggregateKey());
    }
}