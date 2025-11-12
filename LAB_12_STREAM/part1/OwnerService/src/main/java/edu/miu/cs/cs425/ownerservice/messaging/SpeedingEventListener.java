package edu.miu.cs.cs425.ownerservice.messaging;

import edu.miu.cs.cs425.ownerservice.model.Owner;
import edu.miu.cs.cs425.ownerservice.service.OwnerCatalog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SpeedingEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpeedingEventListener.class);

    private final OwnerCatalog ownerCatalog;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String ownerTopic;

    public SpeedingEventListener(OwnerCatalog ownerCatalog,
                                 KafkaTemplate<String, Object> kafkaTemplate,
                                 @Value("${app.topic.owner}") String ownerTopic) {
        this.ownerCatalog = ownerCatalog;
        this.kafkaTemplate = kafkaTemplate;
        this.ownerTopic = ownerTopic;
    }

    @KafkaListener(topics = "${app.topic.speeding}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleSpeedingEvent(SpeedingEvent event) {
        if (event == null || event.getLicencePlate() == null) {
            LOGGER.warn(" ⚠️ Ignoring invalid SpeedingEvent {}", event);
            return;
        }

        ownerCatalog.findOwnerByLicencePlate(event.getLicencePlate())
                .ifPresentOrElse(owner -> processOwnerMatch(event, owner),
                        () -> LOGGER.warn(" 🚫 No owner found for licence plate {}", event.getLicencePlate()));
    }

    private void processOwnerMatch(SpeedingEvent event, Owner owner) {
        LOGGER.info(" 👤 Owner lookup -> {} {} @ {} for plate {}",
                owner.getFirstName(), owner.getLastName(), owner.getAddress(), owner.getLicencePlate());
        LOGGER.info(" ⚙️ Speed recorded: {} mph", String.format("%.2f", event.getSpeedMph()));

        FeeRecord feeRecord = new FeeRecord(
                owner.getLicencePlate(),
                owner.getFirstName(),
                owner.getLastName(),
                owner.getAddress(),
                event.getSpeedMph()
        );

        kafkaTemplate.send(ownerTopic, owner.getLicencePlate(), feeRecord);
        LOGGER.info(" 📬 FeeRecord published to {} for {}", ownerTopic, owner.getLicencePlate());
    }
}

