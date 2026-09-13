package com.example.kafkaorders.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID eventId,
        UUID orderId,
        String product,
        int quantity,
        BigDecimal amount,
        Instant occurredAt
) {}
