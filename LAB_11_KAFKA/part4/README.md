# Kafka Orders Practice

This practice project contains a Spring Boot producer and consumer pair that demonstrate different Kafka consumption scenarios for an `orders-topic`.

## What Was Implemented
- Producer publishes five sample `Order` records (ordernumber, customername, customercountry, amount, status) to `orders-topic` as JSON.
- Default consumer (`gid`) deserializes orders and logs the offset and consumer group.
- Replay consumer (`gid-orders-replay`) starts from the earliest offset (`auto.offset.reset=earliest`) to read the entire history and logs offset and group id.
- Two consumers in the same consumer group (`orders-group`) illustrate how Kafka partitions assign messages. With a single-partition topic both messages go to the same consumer; add more partitions to see distribution.

## Prerequisites
- Java 17+
- Maven
- Apache Kafka running on `localhost:9092` (default Spring configuration)

## How to Run
1. **Start Kafka**
   ```bash
   # In separate terminals
   zookeeper-server-start /path/to/config/zookeeper.properties
   kafka-server-start /path/to/config/server.properties
   ```
2. **Create the topic if needed**
   ```bash
   kafka-topics --create --topic orders-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
   ```
3. **Run the consumer app**
   ```bash
   cd /Users/leone/Documents/MIU/SA/LAB_11_KAFKA/part1/consumer
   ./mvnw spring-boot:run
   ```
   Leave it running to observe offsets, group ids, and which group member receives each message.
4. **Run the producer app**
   ```bash
   cd /Users/leone/Documents/MIU/SA/LAB_11_KAFKA/part1/producer
   ./mvnw spring-boot:run
   ```
   The producer publishes five orders to `orders-topic`.

## Observing Results
- The default consumer logs the orders along with offsets and group `gid`.
- The replay consumer logs the same orders from the beginning even if it starts later.
- The two consumers in `orders-group` log which instance handled each message and the corresponding offset. With a single-partition topic only one instance processes all messages; increase partitions to watch the load spread.

## Cleanup
- Stop the Spring Boot apps with `Ctrl+C`.
- Stop Kafka services if they were started manually.

