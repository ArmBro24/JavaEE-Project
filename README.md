# JavaSpring Microservices

This repository is a backend microservice architecture built with **Spring Boot**, **Apache Kafka**, and **PostgreSQL**, centered around a `PaymentService` for handling transactions and event-driven communication.

> More services like `BookingService` will be added soon.

---

## Microservices in This Repo

- `PaymentService` → Handles payment initiation, refunding, and transaction status updates  
  > Publishes Kafka events: `PaymentCompletedEvent`, `PaymentFailedEvent`

---

## Technologies Used

| Layer           | Stack                                     |
|----------------|--------------------------------------------|
| Language        | Java 17                                   |
| Framework       | Spring Boot 3.x                           |
| Messaging       | Apache Kafka (via Docker Compose)         |
| Database        | PostgreSQL 16 (`PaymentSB`)               |
| Broker Support  | Zookeeper (via Docker)                    |
| Event Format    | JSON payloads over Kafka topics           |
| Dependency Mgmt | Maven                                     |

---

## Kafka Setup (Docker)

To enable Kafka-based messaging, you **must run the Kafka stack before anything else**:

### ▶️ Steps to Run Kafka:

1. Open `kafka/docker-compose.yaml` inside IntelliJ
2. Right-click > `Run 'docker-compose.yaml'`
3. Wait until **Zookeeper** and **Kafka** containers are up in Docker Desktop

> ⚠️ After a while, the containers might stop working. In that case:
> - Open Docker Desktop
> - Delete the Kafka and Zookeeper containers manually
> - Re-run the YAML in IntelliJ

---

## PaymentService Behavior

### Endpoints

| Method | Endpoint                     | Description                         |
|--------|------------------------------|-------------------------------------|
| POST   | `/api/v1/payments`           | Initiate a new payment              |
| GET    | `/api/v1/payments/{id}`      | Retrieve payment by ID              |
| POST   | `/api/v1/payments/refund`    | Trigger a refund for a transaction  |

### Kafka Events

| Event Type             | Trigger                              |
|------------------------|--------------------------------------|
| `PaymentCompletedEvent`| After successful payment              |
| `PaymentFailedEvent`   | After failed attempt                  |
| `RefundCompletedEvent` | After successful refund (future use) |

---

## Database Configuration

Make sure your **PostgreSQL 16** is running.

- Database: `PaymentSB`
- User: `postgres`
- Password: `1234` (adjust in `application.yml` if changed)
- Host: `localhost:5432`

> Tables will be auto-created via JPA (`payments`, `refunds`)

---

## Future Services (Planned)

- `BookingService` → Will trigger events consumed by `PaymentService`
- `LoyaltyService` → Will be notified via Kafka on payment or refund

---

## Requirements

- Docker Desktop 
- IntelliJ IDEA 
- PostgreSQL 16 installed 
- Java 17 
- Maven 

---

## Author

Built with 🔥 by **ArmBro24**  
“Where microservices fly, and bugs die.” ☠️
