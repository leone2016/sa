package edu.miu.cs.cs425.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class OrderListener {
	@KafkaListener(topics = "orders-topic", groupId = "gid")
	// also print group id and offset
	public void listen(String message, 
	@Header(KafkaHeaders.OFFSET) Long offset,
	@Header(KafkaHeaders.GROUP_ID) String groupId) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Order order = objectMapper.readValue(message, Order.class);
			System.out.println("Received Order at offset " + offset + " and group id " + groupId + ": " + order);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
