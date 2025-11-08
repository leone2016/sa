# CQRS Practice – Manual Test Guide

This repository contains three Spring Boot services demonstrating a basic CQRS setup backed by MongoDB:

- `ProductCommandService` (port 8080) – manages product definitions.
- `StockCommandService` (port 8081) – manages quantity on hand for each product.
- `ProductQueryService` (port 8082) – reads from both collections to expose the combined product view.

## Part 2 Update – Event-Sourced Product Commands
- The `ProductCommandService` now persists immutable domain events in the `product_events` collection before updating the product write model (`products` collection).
- Events are versioned and replayed to rebuild the aggregate state, preserving the behaviour from Part 1 while enabling an append-only audit log.
- The write model continues to feed the query side, so existing REST flows and the Part 1 query service still behave the same.

### Verifying the Product Event Log
After running the standard product commands below, inspect the event store:

```bash
mongosh cqrs --eval 'db.product_events.find().sort({occurredOn: 1}).pretty()'
```

Expect to see `CREATED`, `UPDATED`, and `DELETED` events with monotonically increasing `version` values for each `productNumber`.

## Part 2 Update – Event-Sourced Stock Commands
- The `StockCommandService` now appends domain events to `stock_events` before mutating the stock write model (`stocks` collection).
- Aggregate state is reconstructed exclusively by replaying events, so the service validates create/update/delete operations against the event stream rather than the Mongo document.
- The query side still consumes the write model, meaning API responses remain identical to Part 1 while the history is now auditable.

### Verifying the Stock Event Log
Use these commands to confirm the stock event stream:

```bash
curl -X POST http://localhost:8081/stocks \
     -H "Content-Type: application/json" \
     -d '{"productNumber": "P-200", "quantity": 20}'

curl -X PUT http://localhost:8081/stocks/P-200 \
     -H "Content-Type: application/json" \
     -d '{"productNumber": "P-200", "quantity": 32}'

mongosh cqrs --eval 'db.stock_events.find({productNumber: "P-200"}).sort({version: 1})'
```

The event log should show a `CREATED` event followed by an `UPDATED` event with incrementing `version` values. After a delete call, expect a `DELETED` event.

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
### 1. Create a product

   ```bash
   curl -X POST http://localhost:8080/products \
        -H "Content-Type: application/json" \
        -d '{"productNumber": "P-101", "name": "Iphone", "price": 1300.00}'
   ```

### 2. Create stock entry
   ```bash
   curl -X POST http://localhost:8081/stocks \
        -H "Content-Type: application/json" \
        -d '{"productNumber": "P-100", "quantity": 25}'
   ```

### 3. Query the read model
   ```bash
   curl http://localhost:8082/products
   ```
   Expect the response to include `numberInStock: 25`.

## Comprehensive REST Test Matrix

### A. Product command service (port 8080)
- **List products**
  ```bash
  curl http://localhost:8080/products
  ```

- **Create product**
  ```bash
  curl -X POST http://localhost:8080/products \
       -H "Content-Type: application/json" \
       -d '{"productNumber": "P-200", "name": "Mechanical Keyboard", "price": 89.95}'
  ```

- **Update product name/price**
  ```bash
  curl -X PUT http://localhost:8080/products/P-200 \
       -H "Content-Type: application/json" \
       -d '{"productNumber": "P-200", "name": "RGB Mechanical Keyboard", "price": 99.95}'
  
  curl -X PUT http://localhost:8080/products/P-101 \
       -H "Content-Type: application/json" \
       -d '{"productNumber": "P-101", "name": "RGB Mechanical Keyboard", "price": 99.95}'
  ```
  
- **Delete product**
  ```bash
  curl -X DELETE http://localhost:8080/products/P-200
  ```

- **Replay validation**
  ```bash
  curl -X PUT http://localhost:8080/products/P-200 \
       -H "Content-Type: application/json" \
       -d '{"productNumber": "P-200", "name": "Tenkeyless Keyboard", "price": 109.95}'
  
  mongosh cqrs --eval 'db.product_events.find({productNumber: "P-200"}).sort({version: 1})'
  ```
  The last event should reflect the new name/price and its `version` should be greater than the previous event.

### B. Stock command service (port 8081)
- **List stock entries**
  ```bash
  curl http://localhost:8081/stocks
  ```

- **Create stock entry**
  ```bash
  curl -X POST http://localhost:8081/stocks \
       -H "Content-Type: application/json" \
       -d '{"productNumber": "P-200", "quantity": 10}'
  ```

- **Update stock quantity**
  ```bash
  curl -X PUT http://localhost:8081/stocks/P-200 \
       -H "Content-Type: application/json" \
       -d '{"productNumber": "P-200", "quantity": 15}'
  ```

- **Delete stock**
  ```bash
  curl -X DELETE http://localhost:8081/stocks/P-200
  ```

- **Replay validation**
  ```bash
  curl -X PUT http://localhost:8081/stocks/P-200 \
       -H "Content-Type: application/json" \
       -d '{"productNumber": "P-200", "quantity": 45}'

  mongosh cqrs --eval 'db.stock_events.find({productNumber: "P-200"}).sort({version: 1})'
  ```
  Confirm the last event version increases and its payload matches the new quantity.

### C. Product query service (port 8082)
- **Get combined view**
  ```bash
  curl http://localhost:8082/products
  ```
  Verify that each product contains `productNumber`, `name`, `price`, and `numberInStock`.

### D. Suggested validation scenarios
1. **Happy path:** create product + stock, confirm query shows merged data.
2. **Stock missing:** create product only; query should show `numberInStock: 0`.
3. **Product update propagation:** update product name/price; query should reflect new values.
4. **Quantity update propagation:** update stock; query should reflect new quantity.
5. **Product event replay:** update the same product multiple times, then query `product_events` and verify versions increment from 1..n without gaps.
6. **Stock event replay:** repeatedly update a stock entry and confirm `stock_events` maintains a continuous version history.
7. **Deletions:** delete stock (product remains) → query should show `numberInStock: 0`. Then delete product → ensure it disappears from query result and both event streams emit `DELETED`.
8. **Multiple products:** create additional products/stocks to ensure independent aggregation.

## Update & Delete Checks
- Update product: `PUT http://localhost:8080/products/P-100`
- Update quantity: `PUT http://localhost:8081/stocks/P-100`
- Delete product or stock: `DELETE` on the respective endpoint.
- Re-run the query to confirm the read model reflects the latest state.

## Notes
- Each service uses its own Mongo collection (`products`, `stocks`) within the shared `cqrs` database.
- Services expose simple REST endpoints; no authentication or messaging is configured for this exercise.

