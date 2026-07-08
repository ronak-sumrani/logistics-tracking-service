package com.example.logisticstrackingservice.kafka.outbox;

import com.example.logisticstrackingservice.entity.OutboxEvent;
import com.example.logisticstrackingservice.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPoller {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> stringKafkaTemplate;

    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void publishPendingEvents() {
        List<OutboxEvent> pending = outboxEventRepository.findTop50ByPublishedFalseOrderByCreatedAtAsc();

        if (pending.isEmpty()) {
            return;
        }

        log.info("[outbox-poller] found {} pending event(s) to publish", pending.size());

        for (OutboxEvent outboxEvent : pending) {
            try {
                stringKafkaTemplate.send(outboxEvent.getTopic(), outboxEvent.getAggregateKey(), outboxEvent.getPayload())
                        .get();

                outboxEvent.setPublished(true);
                outboxEvent.setPublishedAt(LocalDateTime.now());
                outboxEventRepository.save(outboxEvent);

                log.info("[outbox-poller] published event {} for {}", outboxEvent.getEventId(), outboxEvent.getAggregateKey());
            } catch (Exception e) {
                log.error("[outbox-poller] failed to publish event {}, will retry next cycle: {}",
                        outboxEvent.getEventId(), e.getMessage());
            }
        }
    }
}
