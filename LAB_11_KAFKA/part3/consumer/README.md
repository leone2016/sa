## Order Consumers with Retry and Dead Letter Flows

This module contains two Spring for Apache Kafka consumer flows that demonstrate different retry strategies when a listener throws an `OrderProcessingException`.

### What Was Implemented
- **`DefaultErrorHandlerOrderListener`** uses the container-level `DefaultErrorHandler`.  
  - Retries a failed record 2 additional times with a fixed 1 second delay.  
  - When retries are exhausted, forwards the record to the `orders-topic.DLT` topic where a dedicated listener logs the payload and failure metadata.
- **`RetryableOrderListener`** uses method-level `@Retryable`.  
  - Retries the listener invocation 2 additional times (3 total attempts) with a 1 second backoff.  
  - A `@Recover` method publishes the failed record to `orders-topic.retryable.DLT`, preserving the original headers and adding failure metadata.  
  - A dedicated DLT listener consumes from that topic for auditing.
- **Kafka topics** for the main stream, default DLT, and retryable DLT are created automatically via `KafkaConsumerConfiguration`.
- **Integration tests** in `OrderListenersIntegrationTest` verify that each flow routes failing records to its associated DLT when the triggering status flags (`FAIL_DEFAULT`, `FAIL_RETRYABLE`) are encountered.

### How to Run and Test
1. Ensure Java 21 and Maven are available on your PATH (the Maven wrapper is provided).
2. From this module directory run the tests:
   ```
   ./mvnw test
   ```
   The suite spins up an embedded Kafka broker, publishes failing orders, and asserts that both DLT listeners are invoked.
3. To observe behaviour manually:
   - Start the consumer application:
     ```
     ./mvnw spring-boot:run
     ```
   - Publish orders using the companion producer project (see `../producer`) or any Kafka client.  
   - Use the statuses `FAIL_DEFAULT` or `FAIL_RETRYABLE` to trigger the respective retry and DLT flows and monitor the application logs for retry and DLT messages.

### Notes
- `application.properties` defaults the order topic to `orders-topic`; override `app.kafka.topics.orders` to customise the topic name.
- The `DeadLetterPublishingRecoverer` appends `.DLT` (or `.retryable.DLT`) to the active topic name, so you only need to ensure those topics exist or let the auto-creation config handle them.