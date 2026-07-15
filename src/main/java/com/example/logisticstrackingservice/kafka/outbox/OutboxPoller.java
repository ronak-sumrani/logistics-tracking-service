package com.example.logisticstrackingservice.kafka.outbox;

import com.example.logisticstrackingservice.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPoller {

    private final OutboxEventRepository outboxEventRepository;
    private final OutboxEventProcessor outboxEventProcessor;

    // NO @Transactional annotations here whatsoever
    @Scheduled(fixedDelay = 2000)
    public void publishPendingEvents() {
        List<Long> pendingIds = outboxEventRepository.findPendingEventIds();

        if (pendingIds.isEmpty()) {
            return;
        }

        log.info("[outbox-poller] found {} pending event(s) to publish", pendingIds.size());

        for (Long id : pendingIds) {
            try {
                outboxEventProcessor.processAndPublish(id);
            } catch (Exception e) {
                log.error("[outbox-poller] failed to process event ID {}, will retry next cycle: {}", id, e.getMessage());
            }
        }
    }
}