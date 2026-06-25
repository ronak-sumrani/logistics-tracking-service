package com.example.logisticstrackingservice.enums;

import java.util.List;
import java.util.Map;

public enum ConsignmentStatus {
    CREATED,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED;

    private static final Map<ConsignmentStatus, List<ConsignmentStatus>> VALID_TRANSITIONS = Map.of(
            CREATED, List.of(IN_TRANSIT, CANCELLED),
            IN_TRANSIT, List.of(OUT_FOR_DELIVERY, CANCELLED),
            OUT_FOR_DELIVERY, List.of(DELIVERED, CANCELLED),
            DELIVERED, List.of(),
            CANCELLED, List.of()
    );

    public boolean canTransitionTo(ConsignmentStatus newStatus) {
        return VALID_TRANSITIONS.get(this).contains(newStatus);
    }
}
