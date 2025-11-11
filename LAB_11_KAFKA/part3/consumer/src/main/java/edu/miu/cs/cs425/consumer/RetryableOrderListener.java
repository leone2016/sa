package edu.miu.cs.cs425.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * Order consumer that uses {@link RetryableOrderListener#consumeWithRetry(ConsumerRecord, long, int)}
 * annotated with {@link Retryable} to retry two additional times before forwarding the record
 * into a dedicated retryable dead-letter topic.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RetryableOrderListener {

    static final String RETRYABLE_GROUP_ID = "orders-retryable-group";
    static final String RETRYABLE_DLT_GROUP_ID = "orders-retryable-dlt-group";
    static final String TARGET_STATUS_FLAG = "FAIL_RETRYABLE";
    static final String RETRYABLE_DLT_SUFFIX = ".retryable.DLT";

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(id = "ordersRetryableListener",
            topics = "${app.kafka.topics.orders:orders-topic}",
            groupId = RETRYABLE_GROUP_ID)
    @Retryable(
            retryFor = OrderProcessingException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1_000))
    public void consumeWithRetry(ConsumerRecord<String, String> record) {
        long offset = record.offset();
        int partition = record.partition();
        Order order = readOrder(record);
        log.info("[Retryable] Consumed order {} (status: {}) from partition {} offset {}",
                order.getOrdernumber(), order.getStatus(), partition, offset);
        if (TARGET_STATUS_FLAG.equalsIgnoreCase(order.getStatus())) {
            throw new OrderProcessingException("Retryable flow triggered for order %s"
                    .formatted(order.getOrdernumber()));
        }
    }

    @Recover
    void publishToRetryableDlt(OrderProcessingException exception, ConsumerRecord<String, String> record) {
        ProducerRecord<String, String> producerRecord = buildDltRecord(record, exception);
        kafkaTemplate.send(producerRecord);
        log.warn("[Retryable][DLT Publish] Forwarded record with key {} to topic {} due to {}",
                producerRecord.key(), producerRecord.topic(), exception.getMessage());
    }

    @KafkaListener(id = "ordersRetryableDltListener",
            topics = "${app.kafka.topics.orders:orders-topic}" + RETRYABLE_DLT_SUFFIX,
            groupId = RETRYABLE_DLT_GROUP_ID)
    public void consumeRetryableDlt(String payload,
                                    @Header(name = KafkaHeaders.DLT_EXCEPTION_FQCN, required = false) String exceptionType,
                                    @Header(name = KafkaHeaders.DLT_EXCEPTION_MESSAGE, required = false) String exceptionMessage,
                                    @Header(name = KafkaHeaders.DLT_ORIGINAL_TOPIC, required = false) String originalTopic,
                                    @Header(name = KafkaHeaders.DLT_ORIGINAL_PARTITION, required = false) Integer originalPartition,
                                    @Header(name = KafkaHeaders.DLT_ORIGINAL_OFFSET, required = false) Long originalOffset) {
        log.warn("[Retryable][DLT] payload {} (exception: {} - {}) from {}-{} @{}",
                payload, exceptionType, exceptionMessage, originalTopic, originalPartition, originalOffset);
    }

    private ProducerRecord<String, String> buildDltRecord(ConsumerRecord<String, String> record,
                                                          OrderProcessingException exception) {
        RecordHeaders headers = new RecordHeaders();
        record.headers().forEach(header -> headers.add(header));
        headers.add(KafkaHeaders.DLT_EXCEPTION_FQCN, exception.getClass().getName().getBytes(StandardCharsets.UTF_8));
        String message = exception.getMessage() == null ? "" : exception.getMessage();
        headers.add(KafkaHeaders.DLT_EXCEPTION_MESSAGE, message.getBytes(StandardCharsets.UTF_8));
        headers.add(KafkaHeaders.DLT_ORIGINAL_TOPIC, record.topic().getBytes(StandardCharsets.UTF_8));
        headers.add(KafkaHeaders.DLT_ORIGINAL_PARTITION,
                Integer.toString(record.partition()).getBytes(StandardCharsets.UTF_8));
        headers.add(KafkaHeaders.DLT_ORIGINAL_OFFSET,
                Long.toString(record.offset()).getBytes(StandardCharsets.UTF_8));

        String dltTopic = record.topic() + RETRYABLE_DLT_SUFFIX;
        return new ProducerRecord<>(dltTopic, null, record.timestamp(), record.key(), record.value(), headers);
    }

    private Order readOrder(ConsumerRecord<String, String> record) {
        try {
            return objectMapper.readValue(record.value(), Order.class);
        } catch (Exception ex) {
            throw new OrderProcessingException("Unable to deserialize order payload: " + record.value(), ex);
        }
    }
}

