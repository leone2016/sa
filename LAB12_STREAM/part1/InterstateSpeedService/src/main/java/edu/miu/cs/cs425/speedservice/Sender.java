package edu.miu.cs.cs425.speedservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Sender {

    private final KafkaTemplate<String, SpeedRecord> kafkaTemplate;
    private final String speedingTopic;

    public Sender(KafkaTemplate<String, SpeedRecord> kafkaTemplate,
                  @Value("${app.topic.speeding:tofasttopic}") String speedingTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.speedingTopic = speedingTopic;
    }

    public void send(SpeedRecord speedRecord) {
        kafkaTemplate.send(speedingTopic, speedRecord.getLicencePlate(), speedRecord);
    }
}

