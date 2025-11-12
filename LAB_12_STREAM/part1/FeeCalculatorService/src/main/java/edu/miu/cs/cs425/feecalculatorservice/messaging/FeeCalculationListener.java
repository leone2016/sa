package edu.miu.cs.cs425.feecalculatorservice.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class FeeCalculationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeeCalculationListener.class);

    @KafkaListener(topics = "${app.topic.owner}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleFeeRecord(FeeRecord feeRecord) {
        if (feeRecord == null || feeRecord.getLicencePlate() == null) {
            LOGGER.warn(" ⚠️ Ignoring invalid FeeRecord {}", feeRecord);
            return;
        }

        double feeAmount = calculateFee(feeRecord.getSpeedMph());
        LOGGER.info(" 💰 Fee assessment -> plate {} | {} {} @ {} | speed {} mph | fee ${}",
                feeRecord.getLicencePlate(),
                feeRecord.getFirstName(),
                feeRecord.getLastName(),
                feeRecord.getAddress(),
                String.format("%.2f", feeRecord.getSpeedMph()),
                String.format("%.2f", feeAmount));
    }

    private double calculateFee(double speedMph) {
        if (speedMph >= 72 && speedMph < 77) {
            return 25.0;
        }
        if (speedMph >= 77 && speedMph < 82) {
            return 45.0;
        }
        if (speedMph >= 82 && speedMph <= 90) {
            return 80.0;
        }
        if (speedMph > 90) {
            return 125.0;
        }
        return 0.0;
    }
}

