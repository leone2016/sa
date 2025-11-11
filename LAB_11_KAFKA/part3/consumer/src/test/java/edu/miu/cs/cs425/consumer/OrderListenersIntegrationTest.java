package edu.miu.cs.cs425.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.condition.EmbeddedKafkaCondition;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
@EmbeddedKafka(partitions = 3, topics = {
        "orders-topic",
        "orders-topic.DLT",
        "orders-topic.retryable.DLT"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class OrderListenersIntegrationTest {

    @SpyBean
    DefaultErrorHandlerOrderListener defaultErrorHandlerOrderListener;

    @SpyBean
    RetryableOrderListener retryableOrderListener;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @DynamicPropertySource
    static void overrideKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers",
                () -> EmbeddedKafkaCondition.getBroker().getBrokersAsString());
        registry.add("spring.kafka.producer.bootstrap-servers",
                () -> EmbeddedKafkaCondition.getBroker().getBrokersAsString());
    }

    @Test
    void defaultErrorHandlerRetriesAndRoutesToDlt() throws Exception {
        Order failingOrder = new Order("order-default-1", "Alice", "US", 42.0,
                DefaultErrorHandlerOrderListener.TARGET_STATUS_FLAG);
        kafkaTemplate.send("orders-topic", failingOrder.getOrdernumber(), objectMapper.writeValueAsString(failingOrder))
                .get();

        Mockito.verify(defaultErrorHandlerOrderListener,
                Mockito.timeout(10_000).atLeastOnce())
                .consumeDlt(ArgumentMatchers.contains("\"status\":\"FAIL_DEFAULT\""),
                        ArgumentMatchers.any(), ArgumentMatchers.any(),
                        ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    void retryableConsumerRetriesAndRoutesToDlt() throws Exception {
        Order failingOrder = new Order("order-retryable-1", "Bob", "CA", 55.0,
                RetryableOrderListener.TARGET_STATUS_FLAG);
        kafkaTemplate.send("orders-topic", failingOrder.getOrdernumber(), objectMapper.writeValueAsString(failingOrder))
                .get();

        Mockito.verify(retryableOrderListener,
                Mockito.timeout(10_000).atLeastOnce())
                .consumeRetryableDlt(ArgumentMatchers.contains("\"status\":\"FAIL_RETRYABLE\""),
                        ArgumentMatchers.any(), ArgumentMatchers.any(),
                        ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());
    }
}

