package com.example.kafkaorders.notification;

import com.example.kafkaorders.event.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    @KafkaListener(
            topics = "${app.kafka.topics.orders}",
            groupId = "notification-service"
    )
    public void consume(OrderCreatedEvent event) {
        System.out.println("NOTIFICATION CONSUMED: sending confirmation for order "
                + event.orderId());
    }
}
