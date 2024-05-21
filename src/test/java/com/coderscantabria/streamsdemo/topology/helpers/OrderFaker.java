package com.coderscantabria.streamsdemo.topology.helpers;

import com.coderscantabria.streamsdemo.models.Order;
import lombok.experimental.UtilityClass;
import org.apache.kafka.streams.KeyValue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class OrderFaker {

    public List<KeyValue<String, Order>> getOrders() {

        List<KeyValue<String, Order>> orders = new ArrayList<>();

        Order order1 = new Order("10000", "product_1", new BigDecimal("12.82"));
        Order order2 = new Order("10001", "product_1", new BigDecimal("10.52"));
        Order order3 = new Order("10002", "product_2", new BigDecimal("1.2"));
        Order order4 = new Order("10003", "product_3", new BigDecimal("8.5"));
        Order order5 = new Order("10004", "product_1", new BigDecimal("13.26"));
        Order order6 = new Order("10005", "product_2", new BigDecimal("11.4"));

        orders.add(KeyValue.pair(order1.orderId(), order1));
        orders.add(KeyValue.pair(order2.orderId(), order2));
        orders.add(KeyValue.pair(order3.orderId(), order3));
        orders.add(KeyValue.pair(order4.orderId(), order4));
        orders.add(KeyValue.pair(order5.orderId(), order5));
        orders.add(KeyValue.pair(order6.orderId(), order6));

        return orders;
    }
}
