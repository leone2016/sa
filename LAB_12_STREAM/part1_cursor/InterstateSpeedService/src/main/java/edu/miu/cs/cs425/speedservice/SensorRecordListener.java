package edu.miu.cs.cs425.speedservice;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class SensorRecordListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorRecordListener.class);

    private final SpeedCalculator speedCalculator;

    public SensorRecordListener(SpeedCalculator speedCalculator) {
        this.speedCalculator = speedCalculator;
    }

    @KafkaListener(
            topics = {"cameratopic1", "cameratopic2"},
            containerFactory = "sensorRecordKafkaListenerContainerFactory"
    )
    public void handleSensorRecord(@Payload SensorRecord sensorRecord) {
        if (sensorRecord == null || sensorRecord.getLicencePlate() == null) {
            LOGGER.warn("Received invalid sensor record: {}", sensorRecord);
            return;
        }

        sensorRecord.setReceivedAt(LocalDateTime.now());
        LOGGER.debug("Received sensor record {}", sensorRecord);
        speedCalculator.handleRecord(sensorRecord);
    }
}
