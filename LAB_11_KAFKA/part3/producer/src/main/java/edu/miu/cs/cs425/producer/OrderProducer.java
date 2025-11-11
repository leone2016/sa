package edu.miu.cs.cs425.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class OrderProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendOrder(Order order, String key) throws JsonProcessingException {
        log.info("Sending order with key {}: {}", key, order);
        String orderAsString = objectMapper.writeValueAsString(order);
        kafkaTemplate.send("orders-topic", key, orderAsString);
    }
}
