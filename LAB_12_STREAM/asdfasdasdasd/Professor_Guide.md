# InterstateTrafficSensor Lab – Professor Guide

## Objective
Help students implement an event-driven speed enforcement pipeline using Kafka and Spring Boot. By the end, they should have a set of microservices that simulate traffic sensors, detect speeding vehicles, look up owner information, and optionally calculate fines.

## Learning Outcomes
- Build producer and consumer Kafka clients with Spring.
- Correlate asynchronous events to compute derived metrics (vehicle speed).
- Persist and enrich streaming data across service boundaries.
- Apply clean testing and verification practices to streaming pipelines.

## Prerequisites & Environment
- **Java:** 17 or newer (Java 21 preferred).
- **Build:** Maven (wrapper already in `InterstateTrafficSensor`).
- **Broker:** Apache Kafka (local install or Docker image).
- **Tooling:** IDE with Lombok or equivalent Java support, Docker (optional but recommended), `curl` / Kafka CLI for smoke tests.

### Quick Kafka Setup (Docker)
```bash
docker compose -f docker-compose.kafka.yml up -d
docker compose ps
```
Ensure both ZooKeeper (if used) and Kafka report `Up`. Replace with your lab’s prescribed setup if different.

### Verify Topics
```bash
docker exec -it kafka kafka-topics.sh --create --topic cameratopic1 --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
docker exec -it kafka kafka-topics.sh --create --topic cameratopic2 --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
docker exec -it kafka kafka-topics.sh --create --topic tofasttopic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
docker exec -it kafka kafka-topics.sh --create --topic ownertopic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```
Skip creation if your broker has `auto.create.topics.enable=true`.

## Assignment Roadmap

| Phase | Deliverable | Summary |
|-------|-------------|---------|
| 0 | Environment check | Kafka running, topics created, Maven wrapper executable |
| 1 | Sensor simulator | `InterstateTrafficSensor` producers publish realistic `SensorRecord` messages |
| 2 | InterstateSpeedService | Consumes both camera topics, calculates MPH, emits `SpeedRecord` when > 72 |
| 3 | OwnerService | Subscribes to `tofasttopic`, enriches with owner data, emits `FeeRecord` to `ownertopic` |
| 4 | FeeCalculatorService *(stretch)* | Consumes `ownertopic`, assigns fines, logs summary |
| 5 | Evidence & packaging | Logs, screenshots, README, zipped project |

## Guided Walkthrough

### 1. Explore the Provided Project
- Open `InterstateTrafficSensor` in your IDE.
- Review `kafka/Sensor.java`, `Sensor2.java`, and `Sender.java`. Confirm both producers point to the correct topics and bootstrap servers.
- Skim `SensorRecord.java` to understand the message schema.
- Build once with Maven to warm caches:
```bash
./mvnw -pl InterstateTrafficSensor clean package
```

### 2. Confirm Sensor Output

- Start Kafka (if not already running).
- Run the simulator:
```bash
./mvnw -pl InterstateTrafficSensor spring-boot:run
```
- In a separate terminal consume each camera topic to inspect payloads:
```bash
docker exec -it kafka kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic cameratopic1 \
  --from-beginning
```
- Check that the timestamp fields (`minute`, `second`) change and camera IDs differ.

### 3. Design the Speed Processor
- Create a new Spring Boot module (e.g., `InterstateSpeedService`).
- Dependencies: `spring-kafka`, `spring-boot-starter`.
- Define a `SpeedRecord` DTO containing license plate, timestamps, elapsed seconds, and calculated speed.
- Implement logic:
  1. Buffer incoming `SensorRecord`s keyed by license plate.
  2. When both camera events arrive, compute time difference (`camera2 - camera1`).
  3. Convert to MPH: `mph = 0.5 / (deltaSeconds/3600)`.
  4. If `mph > 72`, publish a `SpeedRecord` to `tofasttopic`.
- Recommended structure:
```java
@Service
public class SpeedProcessor {
    private final KafkaTemplate<String, SpeedRecord> template;
    private final Map<String, SensorRecord> pending = new ConcurrentHashMap<>();

    @KafkaListener(topics = {"cameratopic1", "cameratopic2"})
    public void handleSensor(SensorRecord record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        // ... logic to correlate and compute speed
    }
}
```
- Guard against out-of-order events and stale data (e.g., clear entries older than a minute).

### 4. Create the Owner Service
- Build another Spring Boot application (`OwnerService`).
- Maintain a lookup table (in-memory map or mock repository) from license plate to owner info.
- Consume `SpeedRecord`s from `tofasttopic`:
```java
@KafkaListener(topics = "tofasttopic")
public void handleSpeeding(SpeedRecord record) {
    Owner owner = ownerDirectory.lookup(record.getLicencePlate());
    FeeRecord feeRecord = new FeeRecord(owner, record);
    log.info("Speeding driver: {} at {} mph", owner.getName(), record.getSpeedMph());
    kafkaTemplate.send("ownertopic", feeRecord.getLicencePlate(), feeRecord);
}
```
- Ensure `FeeRecord` captures all data required for the optional fee calculator.

### 5. Implement the Fee Calculator (Optional but Recommended)
- New Spring Boot module or extend OwnerService with another listener.
- Consume `FeeRecord`, compute fee using the provided ranges, log result.
- Consider emitting fines to a `feelogtopic` or persisting to a database if you want extra credit.

## Testing & Verification
- **Unit Tests:** Mock Kafka listeners with `@EmbeddedKafka` or `KafkaTestUtils` to validate speed calculations.
- **Integration Smoke Tests:**
  - Run Kafka.
  - Start all services (`Sensor`, `Speed`, `Owner`, optional `Fee`).
  - Use console consumers to watch each topic and verify transformations:
    - `cameratopic1/2`: raw sensor events.
    - `tofasttopic`: only high-speed events with computed MPH.
    - `ownertopic`: enriched owner data (and optionally fine amounts).
- **Log Checks:** Ensure each service reports key actions (consumed record, computed speed, published record) using structured logging.
- **Edge Case Simulation:** Modify sensor intervals to generate non-speeding traffic and confirm no `SpeedRecord` is emitted.

## Deliverables & Documentation
- README per service explaining:
  - How to run locally.
  - Required environment variables (bootstrap servers, topic names).
  - Test commands (console consumers, unit tests).
- Screenshots or logs showing:
  - Console consumer output for `tofasttopic` and `ownertopic`.
  - Optional: Grafana/Kafdrop views if available.
- Zip package excluding `target/` directories. Include Maven wrappers.

## Grading Alignment
- **Kafka Infrastructure (2 pts):** Topics created, services connect without errors.
- **Speed Detection (3 pts):** Correct MPH calculation above threshold, robust handling of event order.
- **Owner Enrichment (2 pts):** Accurate lookup, informative logging, publishes `FeeRecord`.
- **End-to-End Flow (2 pts):** Demonstrated pipeline from sensors to owner notifications (plus optional fee calculator).
- **Presentation (1 pt):** README clarity, testing evidence, clean repository.

## Troubleshooting Tips
- **Serialization errors:** Confirm `@KafkaListener` and `KafkaTemplate` use matching `Serializer`/`Deserializer` (JSON via `JsonSerializer` recommended).
- **Clock skew:** Use sensor timestamps, not processing time. Handle minute rollover carefully.
- **Duplicate messages:** Include a unique key (`licencePlate + minute + second + cameraId`) to mitigate reprocessing.
- **Topic misconfiguration:** Use `kafka-topics.sh --describe --topic <name>` to inspect partition count and offsets.
- **Trusted package/serialization exceptions:** When consuming `SensorRecord` emitted from the `InterstateTrafficSensor` module (package `kafka.*`), configure Spring Kafka to trust that package and use the `ErrorHandlingDeserializer`. Add to `application.properties`:
  ```
  spring.kafka.consumer.key-deserializer=org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
  spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
  spring.kafka.consumer.properties.spring.deserializer.key.delegate.class=org.apache.kafka.common.serialization.StringDeserializer
  spring.kafka.consumer.properties.spring.deserializer.value.delegate.class=org.springframework.kafka.support.serializer.JsonDeserializer
  spring.kafka.consumer.properties.spring.json.value.default.type=kafka.SensorRecord
  spring.kafka.consumer.properties.spring.json.trusted.packages=kafka.producers,kafka
  ```
  Adjust the fully-qualified class (`kafka.SensorRecord` vs `kafka.producers.SensorRecord`) to match the actual producer package.

## Suggested Enhancements
- Persist speeding events to a database for reporting.
- Expose a REST endpoint summarizing fines per day.
- Add integration tests with an embedded Kafka broker.
- Containerize services with Docker Compose for easier grading and deployment.

Stay systematic: validate each hop (producer → topic → consumer) before layering additional logic. Encourage students to instrument generously and test incrementally.

