package com.coderscantabria.streamsdemo.models;

import java.math.BigDecimal;

public record Order(String orderId, String productId, BigDecimal amount) {
}
