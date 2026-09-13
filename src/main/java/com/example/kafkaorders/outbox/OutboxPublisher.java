package com.example.kafkaorders.outbox;

import com.example.kafkaorders.event.OrderCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OutboxPublisher {

    private final OutboxRepository repository;
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OutboxPublisher(
            OutboxRepository repository,
            KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate,
            ObjectMapper objectMapper) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void publishPendingEvents() {
        for (OutboxEvent event : repository.findTop100ByPublishedFalseOrderByCreatedAtAsc()) {
            try {
                OrderCreatedEvent payload =
                        objectMapper.readValue(event.getPayload(), OrderCreatedEvent.class);

                kafkaTemplate.send(event.getTopic(), event.getMessageKey(), payload).get();
                event.markPublished();
                repository.save(event);
            } catch (Exception e) {
                // Leave it unpublished. The next scheduled run will retry it.
            }
        }
    }
}
