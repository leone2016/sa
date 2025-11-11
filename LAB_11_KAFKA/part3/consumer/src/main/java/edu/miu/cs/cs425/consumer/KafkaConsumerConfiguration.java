package edu.miu.cs.cs425.consumer;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
class KafkaConsumerConfiguration {

    private static final long RETRY_DELAY_MS = 1_000L;
    private static final long MAX_ATTEMPTS = 2L;

    @Bean
    DefaultErrorHandler defaultErrorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {
        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(kafkaTemplate,
                        (record, ex) -> new TopicPartition(record.topic() + ".DLT", record.partition()));

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, new FixedBackOff(RETRY_DELAY_MS, MAX_ATTEMPTS));
        errorHandler.addNotRetryableExceptions(DeserializationException.class);
        return errorHandler;
    }

    @Bean
    org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory,
            DefaultErrorHandler defaultErrorHandler) {
        org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(defaultErrorHandler);
        return factory;
    }

    @Bean
    NewTopic ordersTopic(
            @Value("${app.kafka.topics.orders:orders-topic}") String ordersTopic,
            @Value("${app.kafka.topics.partitions:3}") int partitions) {
        return TopicBuilder.name(ordersTopic)
                .partitions(partitions)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic ordersTopicDlt(
            @Value("${app.kafka.topics.orders:orders-topic}") String ordersTopic,
            @Value("${app.kafka.topics.partitions:3}") int partitions) {
        return TopicBuilder.name(ordersTopic + ".DLT")
                .partitions(partitions)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic ordersRetryableDlt(
            @Value("${app.kafka.topics.orders:orders-topic}") String ordersTopic,
            @Value("${app.kafka.topics.partitions:3}") int partitions) {
        return TopicBuilder.name(ordersTopic + ".retryable.DLT")
                .partitions(partitions)
                .replicas(1)
                .build();
    }
}

