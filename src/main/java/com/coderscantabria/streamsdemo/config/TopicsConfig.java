package com.coderscantabria.streamsdemo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.topics")
@Getter
@Setter
public class TopicsConfig {

    private String ordersTopic;
    private String ordersCountTopic;

}
