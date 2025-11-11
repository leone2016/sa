package edu.miu.cs.cs425.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
class OrderListener {

	private final ObjectMapper objectMapper;

	@KafkaListener(id = "ordersPartition0Listener",
			topicPartitions = @TopicPartition(topic = "orders-topic", partitions = { "0" }),
			groupId = "orders-partitioned-group")
	public void listenPartitionZero(ConsumerRecord<String, String> record) {
		logOrder("Partition-0 listener", record);
	}

	@KafkaListener(id = "ordersPartition1Listener",
			topicPartitions = @TopicPartition(topic = "orders-topic", partitions = { "1" }),
			groupId = "orders-partitioned-group")
	public void listenPartitionOne(ConsumerRecord<String, String> record) {
		logOrder("Partition-1 listener", record);
	}

	@KafkaListener(id = "ordersPartition2Listener",
			topicPartitions = @TopicPartition(topic = "orders-topic", partitions = { "2" }),
			groupId = "orders-partitioned-group")
	public void listenPartitionTwo(ConsumerRecord<String, String> record) {
		logOrder("Partition-2 listener", record);
	}

	private void logOrder(String listenerId, ConsumerRecord<String, String> record) {
		try {
			Order order = objectMapper.readValue(record.value(), Order.class);
			log.info("[{}] Received key {} on partition {} at offset {}: {}",
					listenerId, record.key(), record.partition(), record.offset(), order);
		} catch (Exception e) {
			log.error("[{}] Failed to deserialize order payload from partition {} offset {} with key {}: {}",
					listenerId, record.partition(), record.offset(), record.key(), record.value(), e);
		}
	}
}
