package edu.miu.cs.cs425.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
class OrderListener {

	private final ObjectMapper objectMapper;

	@KafkaListener(id = "ordersPartition0Listener",
			topicPartitions = @TopicPartition(topic = "orders-topic", partitions = { "0" }),
			groupId = "orders-partitioned-group")
	public void listenPartitionZero(String message,
			@Header(KafkaHeaders.OFFSET) Long offset,
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
			@Header(KafkaHeaders.RECEIVED_KEY) String key) {
		logOrder("Partition-0 listener", message, offset, partition, key);
	}

	@KafkaListener(id = "ordersPartition1Listener",
			topicPartitions = @TopicPartition(topic = "orders-topic", partitions = { "1" }),
			groupId = "orders-partitioned-group")
	public void listenPartitionOne(String message,
			@Header(KafkaHeaders.OFFSET) Long offset,
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
			@Header(KafkaHeaders.RECEIVED_KEY) String key) {
		logOrder("Partition-1 listener", message, offset, partition, key);
	}

	@KafkaListener(id = "ordersPartition2Listener",
			topicPartitions = @TopicPartition(topic = "orders-topic", partitions = { "2" }),
			groupId = "orders-partitioned-group")
	public void listenPartitionTwo(String message,
			@Header(KafkaHeaders.OFFSET) Long offset,
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
			@Header(KafkaHeaders.RECEIVED_KEY) String key) {
		logOrder("Partition-2 listener", message, offset, partition, key);
	}

	private void logOrder(String listenerId, String message, Long offset, int partition, String key) {
		try {
			Order order = objectMapper.readValue(message, Order.class);
			log.info("[{}] Received key {} on partition {} at offset {}: {}", listenerId, key, partition, offset, order);
		} catch (Exception e) {
			log.error("[{}] Failed to deserialize order payload from partition {} offset {} with key {}: {}",
					listenerId, partition, offset, key, message, e);
		}
	}
}
