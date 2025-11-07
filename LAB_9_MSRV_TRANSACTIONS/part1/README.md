# CQRS Practice – Manual Test Guide

This repository contains three Spring Boot services demonstrating a basic CQRS setup backed by MongoDB:

- `ProductCommandService` (port 8080) – manages product definitions.
- `StockCommandService` (port 8081) – manages quantity on hand for each product.
- `ProductQueryService` (port 8082) – reads from both collections to expose the combined product view.

## Prerequisites
- MongoDB running on `localhost:27017` (Docker container already configured for the lab).
- Java 21 and Gradle wrappers (included).

## Start the Services
From each service directory, run:

```bash
./gradlew bootRun
```

Start `ProductCommandService`, `StockCommandService`, and finally `ProductQueryService`.

## Smoke Test Flow
1. **Create a product**
   ```bash
   curl -X POST http://localhost:8080/products \
        -H "Content-Type: application/json" \
        -d '{"productNumber": "P-100", "name": "Laptop", "price": 1299.99}'
   ```
2. **Create stock entry**
   ```bash
   curl -X POST http://localhost:8081/stocks \
        -H "Content-Type: application/json" \
        -d '{"productNumber": "P-100", "quantity": 25}'
   ```
3. **Query the read model**
   ```bash
   curl http://localhost:8082/products
   ```
   Expect the response to include `numberInStock: 25`.

## Update & Delete Checks
- Update product: `PUT http://localhost:8080/products/P-100`
- Update quantity: `PUT http://localhost:8081/stocks/P-100`
- Delete product or stock: `DELETE` on the respective endpoint.
- Re-run the query to confirm the read model reflects the latest state.

## Notes
- Each service uses its own Mongo collection (`products`, `stocks`) within the shared `cqrs` database.
- Services expose simple REST endpoints; no authentication or messaging is configured for this exercise.

