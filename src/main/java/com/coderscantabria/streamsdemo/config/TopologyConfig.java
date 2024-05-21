package com.coderscantabria.streamsdemo.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TopologyConfig {

    @UtilityClass
    public static class StoreName {

        public static final String ORDERS_COUNT_STORE = "ORDERS_COUNT_TABLE";
    }
}
