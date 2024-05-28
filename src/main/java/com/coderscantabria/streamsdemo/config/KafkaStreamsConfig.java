package com.coderscantabria.streamsdemo.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.streams.StreamsConfig.*;

@Getter
@Setter
@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaStreamsConfig {

    private final String bootstrapAddress;

    private final String appName;

    public KafkaStreamsConfig(@Value("${spring.kafka.bootstrap-servers}") String bootstrapAddress, @Value("${spring.application.name}") String appName) {

        this.bootstrapAddress = bootstrapAddress;
        this.appName = appName;
    }

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kafkaStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class.getName());
        props.put(COMMIT_INTERVAL_MS_CONFIG, 0);
        props.put(APPLICATION_ID_CONFIG, appName);
        return new KafkaStreamsConfiguration(props);
    }
}
