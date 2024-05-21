package com.coderscantabria.streamsdemo.config;

import com.coderscantabria.streamsdemo.models.Order;
import com.coderscantabria.streamsdemo.models.OrderCountDTO;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonSerde;

@Configuration
@Getter
public class TopicsSerdesFactory {

    private final KafkaStreamsConfiguration kStreamsConfig;

    private final JsonSerde<Order> orderJsonSerde;

    private final JsonSerde<OrderCountDTO> orderCountDTOJsonSerde;

    public TopicsSerdesFactory(KafkaStreamsConfiguration kStreamsConfig) {
        this.kStreamsConfig = kStreamsConfig;
        this.orderJsonSerde = new JsonSerde<>(Order.class);
        this.orderCountDTOJsonSerde = new JsonSerde<>(OrderCountDTO.class);
    }
}
