## Kafka Partitioning Practice

This lab extends the Exercise 1 producer and consumer so that you can observe how Kafka distributes messages across topic partitions.

### What Changed
- The producer now provisions the `orders-topic` with **3 partitions** on startup using a `NewTopic` bean.
- `OrderProducer` sends messages with an explicit key; the key strategy is controlled by `orders.partitioning.mode`.
- There are three dedicated consumers, each bound to a single partition, and each logs the **partition**, **offset**, and **message key** it receives.

### Prerequisites
- Kafka broker running locally on `localhost:9092`.
- Java 17+ and Maven (or use the provided `mvnw` wrapper).

### How to Run
1. **Start Kafka/ZooKeeper** locally (or connect to an existing cluster).
2. **Run the consumers** in one terminal:
   ```bash
   cd /Users/leone/Documents/MIU/SA/LAB_11_KAFKA/part2/consumer
   ./mvnw spring-boot:run
   ```
   You should see three listeners start, one per partition.

3. **Scenario A – Single key (all orders in one partition):**
   - Ensure the producer property is set to the default in `producer/src/main/resources/application.properties`:
     ```
     orders.partitioning.mode=single-key
     ```
   - Run the producer:
     ```bash
     cd /Users/leone/Documents/MIU/SA/LAB_11_KAFKA/part2/producer
     ./mvnw spring-boot:run
     ```
   - Observe in the consumer logs that every order lands on the **same partition** and offsets increment sequentially.

4. **Scenario B – Unique key per order (distributed partitions):**
   - Update the property (or override via `-Dorders.partitioning.mode=unique-key`):
     ```
     orders.partitioning.mode=unique-key
     ```
   - Re-run the producer command above.
   - Check the consumer logs; orders are now balanced across partitions based on their `orderNumber` keys.

### Verifying the Outcome
- Consumers log lines similar to:
  ```text
  [Partition-1 listener] Received key 1003 on partition 1 at offset 2: Order(ordernumber=1003, ...)
  ```
- In Scenario A, only one partition’s listener emits logs.
- In Scenario B, all three listeners receive orders, demonstrating key-based partitioning.

