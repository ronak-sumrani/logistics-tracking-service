package com.example.logisticstrackingservice.kafka.outbox;

import com.example.logisticstrackingservice.entity.OutboxEvent;
import com.example.logisticstrackingservice.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxEventProcessor {

    private final OutboxEventRepository outboxEventRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processOutboxEvent(OutboxEvent outboxEvent) {



        outboxEvent.setPublished(true);
        outboxEvent.setPublishedAt(LocalDateTime.now());
        outboxEventRepository.save(outboxEvent);

        log.info("[outbox-poller] published event {} for {}", outboxEvent.getEventId(), outboxEvent.getAggregateKey());
    }
}
