package com.example.kafkaorders.payment;

import com.example.kafkaorders.event.OrderCreatedEvent;
import com.example.kafkaorders.order.OrderRepository;
import com.example.kafkaorders.order.OrderStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PaymentConsumer {

    private final OrderRepository orderRepository;
    private final ProcessedEventRepository processedEventRepository;
    private final RestClient restClient;

    public PaymentConsumer(OrderRepository orderRepository, ProcessedEventRepository processedEventRepository, RestClient restClient) {
        this.orderRepository = orderRepository;
        this.processedEventRepository = processedEventRepository;
        this.restClient = restClient;
    }

    @KafkaListener(topics = "${app.kafka.topics.orders}", groupId = "payment-service")
    public void consume(OrderCreatedEvent event) {

        System.out.println("CALLING PAYMENT GATEWAY...");

        String response = restClient.post()
                .uri("/fake-payment/charge")
                .retrieve()
                .body(String.class);

        System.out.println("PAYMENT GATEWAY CALL FINISHED");
        System.out.println("PAYMENT GATEWAY RESPONSE: " + response);
        // existing payment logic
        if (event.amount().signum() < 0) {
            throw new IllegalStateException("Negative amount");
        }

        orderRepository.findById(event.orderId()).ifPresent(order -> {
            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);
        });

        processedEventRepository.save(new ProcessedEvent(event.eventId()));

        System.out.println("PAYMENT CONSUMED: " + event.orderId());
    }
}
