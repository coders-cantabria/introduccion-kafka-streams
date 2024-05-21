package com.coderscantabria.streamsdemo.service;

import com.coderscantabria.streamsdemo.config.TopologyConfig;
import com.coderscantabria.streamsdemo.models.OrderCountDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class OrderService {

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    public OrderService(StreamsBuilderFactoryBean streamsBuilderFactoryBean) {

        this.streamsBuilderFactoryBean = streamsBuilderFactoryBean;

    }


    public List<OrderCountDTO> ordersCount() {

        ReadOnlyKeyValueStore<String, Long> ordersDataStore = streamsBuilderFactoryBean.getKafkaStreams()
                .store(StoreQueryParameters.fromNameAndType(
                        TopologyConfig.StoreName.ORDERS_COUNT_STORE,
                        QueryableStoreTypes.keyValueStore()
                ));

        var orders = ordersDataStore.all();
        var splitterator = Spliterators.spliteratorUnknownSize(orders, 0);
        return StreamSupport.stream(splitterator, false)
                .map(keyValue -> new OrderCountDTO(keyValue.key, keyValue.value))
                .toList();

    }

    public Optional<OrderCountDTO> getOrderCountById(String id) {

        ReadOnlyKeyValueStore<String, Long> ordersDataStore = streamsBuilderFactoryBean.getKafkaStreams()
                .store(StoreQueryParameters.fromNameAndType(
                        TopologyConfig.StoreName.ORDERS_COUNT_STORE,
                        QueryableStoreTypes.keyValueStore()
                ));

        var orderCount = ordersDataStore.get(id);
        return orderCount == null? Optional.empty() : Optional.of(new OrderCountDTO(id, orderCount));
    }
}
