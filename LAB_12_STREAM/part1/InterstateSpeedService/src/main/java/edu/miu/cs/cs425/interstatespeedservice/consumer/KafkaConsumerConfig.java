package edu.miu.cs.cs425.interstatespeedservice.consumer;


import edu.miu.cs.cs425.interstatespeedservice.SensorRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, SensorRecord> consumerFactory() {
        JsonDeserializer<SensorRecord> deserializer = new JsonDeserializer<>(SensorRecord.class);
        deserializer.addTrustedPackages("kafka", "kafka.producers",
                "edu.miu.cs.cs425.interstatespeedservice");
        deserializer.ignoreTypeHeaders();             // ignore type headers from producers
        deserializer.setRemoveTypeHeaders(true);
        deserializer.setUseTypeMapperForKey(false);


        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "speed-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean(name = "sensorRecordKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, SensorRecord> sensorRecordKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SensorRecord> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
