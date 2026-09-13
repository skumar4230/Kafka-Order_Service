package com.example.kafkaorders.order;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    protected Order() {}

    public Order(UUID id, String product, int quantity, BigDecimal amount) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.amount = amount;
        this.status = OrderStatus.CREATED;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public BigDecimal getAmount() { return amount; }
    public OrderStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }

    public void setStatus(OrderStatus status) { this.status = status; }
}
