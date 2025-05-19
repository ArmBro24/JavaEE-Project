# JavaSpring Microservices

This repository is a complete backend microservice architecture built with **Spring Boot**, **Apache Kafka**, **Spring Cloud**, and **PostgreSQL**, centered around a `PaymentService`, `BookingService`, and `LoyaltyService`, fully integrated via **Eureka Service Discovery** and **API Gateway**.

---

## Microservices Included

| Service           | Description                                                                 |
|------------------|-----------------------------------------------------------------------------|
| `BookingService` | Initiates a booking and publishes Kafka events to be consumed by Payment    |
| `PaymentService` | Handles payments, refunds, transaction records, and event publishing        |
| `LoyaltyService` | Listens for payment events and accrues loyalty points                       |
| `EurekaService`  | Centralized service registry (discovery layer)                              |
| `APIGateway`     | Routes all client-facing API requests via a single port (`8080`)            |

---

## Technologies Used

| Layer           | Stack                                     |
|----------------|--------------------------------------------|
| Language        | Java 17                                   |
| Framework       | Spring Boot 3.4.4                         |
| Messaging       | Apache Kafka                              |
| Service Registry| Spring Cloud Netflix Eureka               |
| Routing         | Spring Cloud Gateway                      |
| Database        | PostgreSQL 16                             |
| Broker Support  | Zookeeper (via Docker)                    |
| Dependency Mgmt | Maven                                     |
| Container Infra | Docker Compose                            |

---

## Kafka Setup (Docker)

To enable event-driven communication, **start Kafka before running services**.

### Run Kafka:

1. Open `kafka/docker-compose.yaml` in IntelliJ or terminal
2. Run via IntelliJ “Compose Deployment” or:
   ```bash
   docker-compose -f kafka/docker-compose.yaml up

If Kafka crashes later:

Restart containers in Docker Desktop or

Remove existing ones and rerun

API Gateway Routing (Port 8080)
Path	Routed To
/api/v1/bookings/**	BookingService
/api/v1/payments/**	PaymentService
/api/v1/loyalty/**	LoyaltyService

All endpoints are accessible from a single port (8080) — thanks to Eureka + Gateway.

Kafka Events Flow
From	To	Event Type
BookingService	PaymentService	BookingCreatedEvent
PaymentService	LoyaltyService	PaymentCompletedEvent
PaymentService	(Optional)	RefundCompletedEvent

Database Configuration
Each service uses its own PostgreSQL DB:

Service	Database Name	Port
BookingService	BookingDB	5432
PaymentService	PaymentSB	5432
LoyaltyService	LoyaltyDB	5432

Default creds:

Username: arm

Password: 1234

Modify in application.properties if needed

✅ Requirements
Docker Desktop

IntelliJ IDEA

PostgreSQL 16

Java 17

Maven 3.9.x+

Notes
Eureka must be started before other services

Gateway must be started last to ensure all routing works

Kafka must be running for Booking → Payment → Loyalty flow

Built by ArmBro24
Microservices, Kafka, Eureka & Gateway — fully alive on port 8080
