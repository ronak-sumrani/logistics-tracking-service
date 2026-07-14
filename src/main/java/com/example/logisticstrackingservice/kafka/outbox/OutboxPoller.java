package com.example.logisticstrackingservice.kafka.outbox;

import com.example.logisticstrackingservice.entity.OutboxEvent;
import com.example.logisticstrackingservice.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPoller {

    private final OutboxEventRepository outboxEventRepository;
    private final OutboxEventProcessor outboxEventProcessor;
    private final KafkaTemplate<String, String> stringKafkaTemplate;

    @Transactional
    @Scheduled(fixedDelay = 2000)
    public void publishPendingEvents() {
        List<OutboxEvent> pending = outboxEventRepository.findTop50ByPublishedFalseOrderByCreatedAtAsc();

        if (pending.isEmpty()) {
            return;
        }

        log.info("[outbox-poller] found {} pending event(s) to publish", pending.size());

        for (OutboxEvent outboxEvent : pending) {
            try{
                if(!outboxEventRepository.existsByEventId(outboxEvent.getEventId())) {
                    stringKafkaTemplate.send(outboxEvent.getTopic(), outboxEvent.getAggregateKey(), outboxEvent.getPayload());
                    outboxEventProcessor.processOutboxEvent(outboxEvent);
                }
            } catch (Exception e) {
                log.error("[outbox-poller] failed to publish event {}, will retry next cycle: {}", outboxEvent.getEventId(), e.getMessage());
            }
        }
    }
}
