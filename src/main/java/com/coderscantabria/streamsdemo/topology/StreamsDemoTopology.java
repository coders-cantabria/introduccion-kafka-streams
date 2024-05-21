package com.coderscantabria.streamsdemo.topology;

import com.coderscantabria.streamsdemo.config.TopicsConfig;
import com.coderscantabria.streamsdemo.config.TopicsSerdesFactory;
import com.coderscantabria.streamsdemo.config.TopologyConfig;
import com.coderscantabria.streamsdemo.models.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StreamsDemoTopology {

    private final TopicsConfig topicsConfig;
    private final TopicsSerdesFactory topicsSerdesFactory;

    @Autowired
    public void process(StreamsBuilder streamsBuilder) {

        KTable<String, Long> ordersCount = getOrdersStream(streamsBuilder)
                .peek((key, value) -> log.info("Received order {} of product {}", key, value.productId()))
                .map((key, value) -> KeyValue.pair(value.productId(), value))
                .groupByKey(Grouped.with(Serdes.String(), topicsSerdesFactory.getOrderJsonSerde()))
                .count(Named.as(topicsConfig.getOrdersCountTopic()), Materialized.as(TopologyConfig.StoreName.ORDERS_COUNT_STORE));

        ordersCount
                .toStream()
                .peek((key, value) -> log.info("Count update. Product {} - count orders: {}", key, value))
                .to(topicsConfig.getOrdersCountTopic(), Produced.with(Serdes.String(), Serdes.Long()));

    }

    private KStream<String, Order> getOrdersStream(StreamsBuilder streamsBuilder) {

        return streamsBuilder.stream(topicsConfig.getOrdersTopic(), Consumed.with(Serdes.String(), topicsSerdesFactory.getOrderJsonSerde()));
    }
}
