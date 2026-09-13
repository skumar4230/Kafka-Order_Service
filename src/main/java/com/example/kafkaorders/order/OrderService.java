package com.example.kafkaorders.order;

import com.example.kafkaorders.event.OrderCreatedEvent;
import com.example.kafkaorders.outbox.OutboxEvent;
import com.example.kafkaorders.outbox.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepository, OutboxRepository outboxRepository, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Order create(CreateOrderRequest request) {
        UUID orderId = UUID.randomUUID();

        Order order = new Order(
                orderId,
                request.product(),
                request.quantity(),
                request.amount()
        );

        orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(
                UUID.randomUUID(),
                order.getId(),
                order.getProduct(),
                order.getQuantity(),
                order.getAmount(),
                order.getCreatedAt()
        );

        outboxRepository.save(OutboxEvent.from("orders.v1", orderId.toString(), event,objectMapper));

        return order;
    }
}
