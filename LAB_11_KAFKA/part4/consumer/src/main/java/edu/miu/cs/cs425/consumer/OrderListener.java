package edu.miu.cs.cs425.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class OrderListener {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@KafkaListener(id = "ordersDefaultConsumer", topics = "orders-topic", groupId = "gid")
	public void listenDefaultGroup(String message,
			@Header(KafkaHeaders.OFFSET) Long offset,
			@Header(KafkaHeaders.GROUP_ID) String groupId) {
		logOrder("Default consumer", message, offset, groupId);
	}

	@KafkaListener(id = "ordersReplayConsumer", topics = "orders-topic", groupId = "gid-orders-replay", properties = {
			"auto.offset.reset=earliest"
	})
	public void listenFromBeginning(String message,
			@Header(KafkaHeaders.OFFSET) Long offset,
			@Header(KafkaHeaders.GROUP_ID) String groupId) {
		logOrder("Replay consumer", message, offset, groupId);
	}

	@KafkaListener(id = "ordersGroupConsumer1", topics = "orders-topic", groupId = "orders-group")
	public void listenGroupConsumerOne(String message,
			@Header(KafkaHeaders.OFFSET) Long offset,
			@Header(KafkaHeaders.GROUP_ID) String groupId) {
		logOrder("Group consumer #1", message, offset, groupId);
	}

	@KafkaListener(id = "ordersGroupConsumer2", topics = "orders-topic", groupId = "orders-group")
	public void listenGroupConsumerTwo(String message,
			@Header(KafkaHeaders.OFFSET) Long offset,
			@Header(KafkaHeaders.GROUP_ID) String groupId) {
		logOrder("Group consumer #2", message, offset, groupId);
	}

	private void logOrder(String listenerId, String message, Long offset, String groupId) {
		try {
			Order order = objectMapper.readValue(message, Order.class);
			log.info("[{}] Received Order at offset {} (group {}): {}", listenerId, offset, groupId, order);
		} catch (Exception e) {
			log.error("[{}] Failed to deserialize order payload: {}", listenerId, message, e);
		}
	}
}
