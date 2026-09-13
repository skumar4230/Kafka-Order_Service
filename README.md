# Kafka Order System — Hands-on Learning Project

This is intentionally a single Spring Boot application that demonstrates the architecture of a real event-driven order system without forcing you to manage multiple repositories at the beginning.

## Stack

- Java 21
- Spring Boot 3.5
- Spring Kafka
- Apache Kafka 4.x (KRaft, single broker for learning)
- PostgreSQL 17
- Redis 7
- Maven

## Architecture

REST -> Order Service -> PostgreSQL + Outbox -> Kafka -> Payment / Inventory / Notification

Redis is used separately as a cache. It is NOT a replacement for Kafka or PostgreSQL.

## Start infrastructure

```bash
docker compose up -d
```

Check:

```bash
docker ps
```

You should see Kafka, PostgreSQL and Redis running.

## Run Spring Boot

Windows PowerShell:

```powershell
mvnw.cmd spring-boot:run
```

or, if Maven is installed:

```powershell
mvn spring-boot:run
```

## Create an order

PowerShell:

```powershell
curl.exe -X POST http://localhost:8080/orders `
  -H "Content-Type: application/json" `
  -d "{\"product\":\"Laptop\",\"quantity\":2,\"amount\":120000}"
```

The request first creates the order and an outbox record in the SAME PostgreSQL transaction.

A scheduled publisher then sends the outbox event to Kafka.

Watch the Spring Boot console. You should see:

```text
PAYMENT CONSUMED: ...
INVENTORY CONSUMED: ...
NOTIFICATION CONSUMED: ...
```

## Why three consumers all receive the event

They have different group IDs:

- payment-service
- inventory-service
- notification-service

Therefore Kafka treats them as three independent subscribers.

## Inspect Kafka

If you want a shell inside the Kafka container:

```bash
docker exec -it kafka_order_demo /bin/bash
```

Then:

```bash
/opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
```

Describe the topic:

```bash
/opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic orders.v1
```

Consume manually:

```bash
/opt/kafka/bin/kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic orders.v1 \
  --from-beginning
```

On Windows PowerShell, the multiline command can be entered as one line if preferred.

## Redis demo

Open:

```text
GET http://localhost:8080/products/123
```

First request:

```text
CACHE MISS -> DB -> REDIS
```

Second request:

```text
CACHE HIT
```

The point is to see that Redis is an acceleration layer, while Kafka is an event/streaming layer.

## Important learning stages

1. Run the system.
2. Understand OrderController -> OrderService -> PostgreSQL.
3. Understand the Outbox table and why it exists.
4. Follow OutboxPublisher -> KafkaTemplate.
5. Follow Kafka -> three @KafkaListener methods.
6. Understand consumer groups.
7. Change partition count / run multiple instances and observe scaling.
8. Add retry + DLT.
9. Break a consumer and inspect offsets.
10. Test duplicate events and idempotency.
11. Measure Redis cache hit vs DB access.
12. Discuss throughput, batching, compression and backpressure.

## Production note

This project is educational. The outbox publisher uses a simple scheduled polling implementation and waits for the Kafka send to complete. In a production system you would normally improve this with stronger concurrency/claiming semantics, monitoring, retry policies and operational controls.

Also, the demo uses one Kafka broker with replication factor 1. A production Kafka cluster would use multiple brokers and appropriate replication.
