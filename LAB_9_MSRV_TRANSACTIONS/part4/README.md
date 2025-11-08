## Flight Booking Saga System

This project demonstrates a distributed transaction across three Spring Boot microservices (Seat, Payment, Ticket) using the Saga **choreography** pattern with RabbitMQ events.

- `seat-service` reserves seats in MongoDB.
- `payment-service` processes and refunds payments in MongoDB.
- `ticket-service` issues and cancels tickets in MongoDB.
- `common` contains shared DTOs, events, and utilities referenced by every service.
- `saga-orchestrator` remains in the repository for reference to the previous orchestration implementation and is no longer required for the booking flow.

### Event Flow
1. Client calls `seat-service` to reserve a seat. On success it publishes a `SeatReservedEvent`.
2. `payment-service` listens for `SeatReservedEvent`, processes the payment, and publishes either `PaymentCompletedEvent` or `PaymentFailedEvent`.
3. `ticket-service` listens for `PaymentCompletedEvent`, attempts to issue the ticket, and publishes either `TicketIssuedEvent` or `TicketIssueFailedEvent`.
4. Compensation: `seat-service` cancels reservations when it receives `PaymentFailedEvent` or `TicketIssueFailedEvent`, publishing `BookingCancelledEvent`. `payment-service` refunds payments when it receives `TicketIssueFailedEvent`.

### Prerequisites
- Java 21
- MongoDB (running locally or accessible via connection string)
- RabbitMQ (v3.13+) for event choreography
- Gradle (Gradle wrapper included)

### Running the Services
Start MongoDB with three logical databases (`seatdb`, `paymentdb`, `ticketdb`) and RabbitMQ. Using Docker:

```bash
docker run -d --name flight-mongo -p 27017:27017 mongo:7
docker run -d --name flight-rabbit -p 5672:5672 -p 15672:15672 rabbitmq:3.13-management
```

Each service is an independent Gradle project. From the repository root run each service in its own terminal:

```bash
./seat-service/gradlew -p seat-service bootRun
./payment-service/gradlew -p payment-service bootRun
./ticket-service/gradlew -p ticket-service bootRun
```

The services listen on:
- `seat-service`: `http://localhost:8081`
- `payment-service`: `http://localhost:8082`
- `ticket-service`: `http://localhost:8083`

All URLs, MongoDB connections, and RabbitMQ connections are overridable through environment variables (`SEAT_SERVICE_URL`, `SEAT_MONGO_URI`, `RABBITMQ_HOST`, etc.).

### Booking Flow
Send a booking request directly to the seat service:

```bash
curl -X POST http://localhost:8081/api/seats/reserve \
  -H "Content-Type: application/json" \
  -d '{
        "bookingId": "BKG-1001",
        "flightNumber": "FL-001",
        "seatNumber": "12A",
        "customerId": "CUST-42",
        "amount": 250.00,
        "currency": "USD",
        "paymentMethod": "VISA"
      }'
```

### Failure Simulation
- Use a payment method containing `DECLINE` or `FAIL` to trigger a payment error and seat compensation.
- Use a seat number containing `ERR` to trigger a ticket issuance error. The event choreography automatically refunds the payment and cancels the seat reservation.

### Tests

Each Gradle project includes the `bootTest` task:

```bash
./seat-service/gradlew -p seat-service test
./payment-service/gradlew -p payment-service test
./ticket-service/gradlew -p ticket-service test
```

### Notes
- The services communicate asynchronously via RabbitMQ topic exchange `booking.events`.
- REST endpoints remain for direct service access; the saga progression is entirely event-driven.
- MongoDB auto-index creation is enabled; no additional schema setup is required.
- Shared DTOs and saga event utilities reside under `common/src/main/java/com/flight/common/`.

