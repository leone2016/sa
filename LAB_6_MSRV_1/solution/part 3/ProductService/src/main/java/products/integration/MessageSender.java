package products.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import products.service.dto.ProductChangeEventDTO;

@Component
public class MessageSender {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(ProductChangeEventDTO event)  {
        try {
            //convert person to JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            String eventString = objectMapper.writeValueAsString(event);
            System.out.println("Sending a JMS message:" + eventString);
            kafkaTemplate.send("testQueue", eventString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
