package edu.miu.cs.cs425.interstatespeedservice.producer;

import edu.miu.cs.cs425.interstatespeedservice.SpeedRecordDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Sender {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpeedCalculator.class);

    private final KafkaTemplate<String, SpeedRecordDTO> kafkaTemplate;
    private final String speedingTopic;

    public Sender(KafkaTemplate<String, SpeedRecordDTO> kafkaTemplate,
                  @Value("${app.topic.speeding:tofasttopic}") String speedingTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.speedingTopic = speedingTopic;
    }

    public void send(SpeedRecordDTO speedRecord) {
        LOGGER.debug("[SENDER] Car is speeding : {}", speedRecord.getLicencePlate());
        kafkaTemplate.send(speedingTopic, speedRecord.getLicencePlate(), speedRecord);
    }
}

