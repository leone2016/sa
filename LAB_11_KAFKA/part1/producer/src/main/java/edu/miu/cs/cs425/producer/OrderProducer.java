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
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendOrder(Order order) throws JsonProcessingException {
        log.info("Sending order: {}", order);
        ObjectMapper mapper = new ObjectMapper();
        String orderAsString = mapper.writeValueAsString(order);
        kafkaTemplate.send("orders-topic", order.toString());
    }
}
