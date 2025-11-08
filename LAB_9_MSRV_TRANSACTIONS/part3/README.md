## Flight Booking Saga System

This project demonstrates a distributed transaction across three Spring Boot microservices (Seat, Payment, Ticket) using the Saga orchestration pattern.

- `seat-service` reserves seats in MongoDB.
- `payment-service` processes and refunds payments in MongoDB.
- `ticket-service` issues and cancels tickets in MongoDB.
- `saga-orchestrator` coordinates the saga and drives compensating actions when failures occur.
- `common` contains shared DTOs, events, and utilities referenced by every service.

### Prerequisites
- Java 21
- MongoDB (running locally or accessible via connection string)
- Gradle (Gradle wrapper included)

### Running the Services
Start MongoDB with three logical databases (`seatdb`, `paymentdb`, `ticketdb`). Using Docker:

```bash
docker run -d --name flight-mongo -p 27017:27017 mongo:7
```

Each service is an independent Gradle project. From the repository root run each service in its own terminal:

```bash
./seat-service/gradlew -p seat-service bootRun
./payment-service/gradlew -p payment-service bootRun
./ticket-service/gradlew -p ticket-service bootRun
./saga-orchestrator/gradlew -p saga-orchestrator bootRun
```

The services listen on:
- `seat-service`: `http://localhost:8081`
- `payment-service`: `http://localhost:8082`
- `ticket-service`: `http://localhost:8083`
- `saga-orchestrator`: `http://localhost:8080`

All URLs and MongoDB connections are overridable through environment variables (`SEAT_SERVICE_URL`, `SEAT_MONGO_URI`, etc.).

### Booking Flow
Send a booking request to the orchestrator:

```bash
curl -X POST http://localhost:8080/api/bookings \
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
- Use a seat number containing `ERR` to trigger a ticket issuance error. The orchestrator automatically refunds the payment and cancels the seat reservation.

### Tests

Each Gradle project includes the `bootTest` task:

```bash
./seat-service/gradlew -p seat-service test
./payment-service/gradlew -p payment-service test
./ticket-service/gradlew -p ticket-service test
./saga-orchestrator/gradlew -p saga-orchestrator test
```

### Notes
- The services communicate synchronously over REST using OpenFeign.
- MongoDB auto-index creation is enabled; no additional schema setup is required.
- Shared DTOs and saga event utilities reside under `common/src/main/java/com/flight/common/`.

