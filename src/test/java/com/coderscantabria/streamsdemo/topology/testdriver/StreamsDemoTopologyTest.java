package com.coderscantabria.streamsdemo.topology.testdriver;

import com.coderscantabria.streamsdemo.config.KafkaStreamsConfig;
import com.coderscantabria.streamsdemo.config.TopicsConfig;
import com.coderscantabria.streamsdemo.config.TopicsSerdesFactory;
import com.coderscantabria.streamsdemo.config.TopologyConfig;
import com.coderscantabria.streamsdemo.models.Order;
import com.coderscantabria.streamsdemo.topology.StreamsDemoTopology;
import com.coderscantabria.streamsdemo.topology.helpers.OrderFaker;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StreamsDemoTopologyTest {

    TopologyTestDriver testDriver = null;
    TestInputTopic<String, Order> inputTopic = null;
    TestOutputTopic<String, Long> outputTopic = null;
    static final String INPUT_TOPIC = "ORDERS";
    static final String OUTPUT_TOPIC = "ORDERS_COUNT";
    StreamsBuilder streamsBuilder;
    StreamsDemoTopology ordersTopology;
    TopicsConfig topicsConfig;
    KafkaStreamsConfig streamsConfig;
    TopicsSerdesFactory serdesFactory;

    @BeforeEach
    void setUp() {
        topicsConfig = new TopicsConfig();
        topicsConfig.setOrdersTopic(INPUT_TOPIC);
        topicsConfig.setOrdersCountTopic(OUTPUT_TOPIC);

        streamsConfig = new KafkaStreamsConfig("localhost", "app-name");
        serdesFactory = new TopicsSerdesFactory(streamsConfig.kafkaStreamsConfig());
        ordersTopology = new StreamsDemoTopology(topicsConfig, serdesFactory);

        streamsBuilder = new StreamsBuilder();

        ordersTopology.process(streamsBuilder);
        Topology topology = streamsBuilder.build();
        testDriver = new TopologyTestDriver(topology);

        inputTopic = testDriver.createInputTopic(
                INPUT_TOPIC, Serdes.String().serializer(),
                serdesFactory.getOrderJsonSerde().serializer());

        outputTopic = testDriver.createOutputTopic(
                OUTPUT_TOPIC,
                Serdes.String().deserializer(),
                Serdes.Long().deserializer());

        describe(topology);
    }

    @AfterEach
    void tearDown() {
        testDriver.close();
    }

    @Test
    void testProcessAccessingToStateStore() {

        inputTopic.pipeKeyValueList(OrderFaker.getOrders());

        ReadOnlyKeyValueStore<String, Long> ordersCountStorage = testDriver.getKeyValueStore(TopologyConfig.StoreName.ORDERS_COUNT_STORE);

        assertEquals(3, ordersCountStorage.get("product_1"));
        assertEquals(2, ordersCountStorage.get("product_2"));
        assertEquals(1, ordersCountStorage.get("product_3"));
    }

    @Test
    void testProcessAccessingToOutputTopic() {

        List<KeyValue<String, Order>> orders = OrderFaker.getOrders();
        inputTopic.pipeKeyValueList(orders);

        List<KeyValue<String, Long>> ordersCountTopic = outputTopic.readKeyValuesToList();

        assertEquals(orders.size(), ordersCountTopic.size());
        assertEquals("product_1", ordersCountTopic.get(0).key);
        assertEquals(1, ordersCountTopic.get(0).value);
        assertEquals("product_1", ordersCountTopic.get(1).key);
        assertEquals(2, ordersCountTopic.get(1).value);
        assertEquals("product_1", ordersCountTopic.get(4).key);
        assertEquals(3, ordersCountTopic.get(4).value);
    }

    private void describe(Topology topology) {

        System.out.println("[ STREAMS DEMO TOPOLOGY DESCRIBE START ]\n");
        System.out.println(topology.describe());
        System.out.println("[ STREAMS DEMO TOPOLOGY DESCRIBE END]");
    }

}