package edu.miu.cs.cs425.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * Order consumer that relies on a {@link org.springframework.kafka.listener.DefaultErrorHandler}
 * configured in {@link KafkaConsumerConfiguration}. When an {@link OrderProcessingException}
 * is thrown it retries two additional times and then publishes the record to the
 * dead-letter topic {@code orders-topic.DLT}.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class DefaultErrorHandlerOrderListener {

    static final String DEFAULT_GROUP_ID = "orders-default-error-handler-group";
    static final String DEFAULT_DLT_GROUP_ID = "orders-default-error-handler-dlt-group";
    static final String TARGET_STATUS_FLAG = "FAIL_DEFAULT";

    private final ObjectMapper objectMapper;

    @KafkaListener(id = "ordersDefaultErrorHandlerListener",
            topics = "${app.kafka.topics.orders:orders-topic}",
            groupId = DEFAULT_GROUP_ID)
    public void consume(ConsumerRecord<String, String> record) {
        long offset = record.offset();
        int partition = record.partition();
        Order order = readOrder(record);
        log.info("[DefaultErrorHandler] Consumed order {} (status: {}) from partition {} offset {}",
                order.getOrdernumber(), order.getStatus(), partition, offset);
        if (TARGET_STATUS_FLAG.equalsIgnoreCase(order.getStatus())) {
            throw new OrderProcessingException("DefaultErrorHandler flow triggered for order %s"
                    .formatted(order.getOrdernumber()));
        }
    }

    @KafkaListener(id = "ordersDefaultErrorHandlerDltListener",
            topics = "${app.kafka.topics.orders:orders-topic}.DLT",
            groupId = DEFAULT_DLT_GROUP_ID)
    public void consumeDlt(String payload,
                           @Header(name = KafkaHeaders.DLT_EXCEPTION_FQCN, required = false) String exceptionType,
                           @Header(name = KafkaHeaders.DLT_EXCEPTION_MESSAGE, required = false) String exceptionMessage,
                           @Header(name = KafkaHeaders.DLT_ORIGINAL_TOPIC, required = false) String originalTopic,
                           @Header(name = KafkaHeaders.DLT_ORIGINAL_PARTITION, required = false) String originalPartition,
                           @Header(name = KafkaHeaders.DLT_ORIGINAL_OFFSET, required = false) String originalOffset) {
        log.warn("[DefaultErrorHandler][DLT] Received payload {} (exception: {} - {}) from {}-{} @{}",
                payload, exceptionType, exceptionMessage, originalTopic, originalPartition, originalOffset);
    }

    private Order readOrder(ConsumerRecord<String, String> record) {
        try {
            return objectMapper.readValue(record.value(), Order.class);
        }
        catch (Exception ex) {
            throw new OrderProcessingException("Unable to deserialize order payload: " + record.value(), ex);
        }
    }
}

