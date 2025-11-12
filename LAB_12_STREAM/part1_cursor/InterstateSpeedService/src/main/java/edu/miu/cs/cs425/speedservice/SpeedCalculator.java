package edu.miu.cs.cs425.speedservice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SpeedCalculator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpeedCalculator.class);
    private static final double DISTANCE_MILES = 0.5;
    private static final double SECONDS_TO_HOURS = 3600.0;
    private static final double SPEED_THRESHOLD_MPH = 72.0;

    private final Sender sender;
    private final Map<String, SensorRecord> recordStream = new ConcurrentHashMap<>();
    private final OwnerClient ownerClient;

    public SpeedCalculator(Sender sender, OwnerClient ownerClient) {
        this.sender = sender;
        this.ownerClient = ownerClient;
    }

    public void handleRecord(SensorRecord sensorRecord) {
        if (sensorRecord.getCameraId() == 1) {
            recordStream.put(sensorRecord.getLicencePlate(), sensorRecord);
            LOGGER.debug("Captured first camera reading for {}", sensorRecord.getLicencePlate());
            return;
        }

        if (sensorRecord.getCameraId() != 2) {
            LOGGER.warn("Unexpected camera id {} for plate {}", sensorRecord.getCameraId(), sensorRecord.getLicencePlate());
            return;
        }

        SensorRecord firstReading = recordStream.remove(sensorRecord.getLicencePlate());
        if (firstReading == null) {
            LOGGER.debug("No matching record from camera 1 for {}", sensorRecord.getLicencePlate());
            return;
        }

        int elapsedSeconds = calculateElapsedSeconds(firstReading, sensorRecord);
        if (elapsedSeconds <= 0) {
            LOGGER.debug("Invalid elapsed time {} for {}", elapsedSeconds, sensorRecord.getLicencePlate());
            return;
        }

        double speedMph = (DISTANCE_MILES / elapsedSeconds) * SECONDS_TO_HOURS;
        LOGGER.info("Vehicle {} speed {:.2f} mph", sensorRecord.getLicencePlate(), speedMph);

        if (speedMph > SPEED_THRESHOLD_MPH) {
            LOGGER.warn("Vehicle {} speeding at {:.2f} mph", sensorRecord.getLicencePlate(), speedMph);
            sender.send(new SpeedRecord(sensorRecord.getLicencePlate(), speedMph));
            ownerClient.findOwner(sensorRecord.getLicencePlate())
                    .ifPresentOrElse(
                            owner -> LOGGER.info("Owner for {}: {} {} - {}", owner.getLicencePlate(),
                                    owner.getFirstName(), owner.getLastName(), owner.getAddress()),
                            () -> LOGGER.warn("No owner information found for {}", sensorRecord.getLicencePlate())
                    );
        }
    }

    private int calculateElapsedSeconds(SensorRecord camera1Record, SensorRecord camera2Record) {
        int minutesDiff = camera2Record.getMinute() - camera1Record.getMinute();
        int secondsDiff = camera2Record.getSecond() - camera1Record.getSecond();

        if (minutesDiff < 0) {
            minutesDiff += 60;
        }

        return minutesDiff * 60 + secondsDiff;
    }
}
