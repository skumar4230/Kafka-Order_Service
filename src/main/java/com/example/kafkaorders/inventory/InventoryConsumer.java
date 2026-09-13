package com.example.kafkaorders.inventory;

import com.example.kafkaorders.event.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryConsumer {

    @KafkaListener(
            topics = "${app.kafka.topics.orders}",
            groupId = "inventory-service"
    )
    public void consume(OrderCreatedEvent event) {
        System.out.println("INVENTORY CONSUMED: reserving " +
                event.quantity() + " x " + event.product() +
                " for order " + event.orderId());
    }
}
