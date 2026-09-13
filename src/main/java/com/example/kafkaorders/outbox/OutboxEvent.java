package com.example.kafkaorders.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
public class OutboxEvent {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String messageKey;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private boolean published;

    protected OutboxEvent() {
    }

    public static OutboxEvent from(String topic, String key, Object payloadObject, ObjectMapper objectMapper) {

        try {
            OutboxEvent event = new OutboxEvent();
            event.id = UUID.randomUUID();
            event.topic = topic;
            event.messageKey = key;
            event.payload = objectMapper.writeValueAsString(payloadObject);
            event.createdAt = Instant.now();
            event.published = false;
            return event;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not serialize outbox event", e);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String getPayload() {
        return payload;
    }

    public boolean isPublished() {
        return published;
    }

    public void markPublished() {
        this.published = true;
    }
}
