package com.coderscantabria.streamsdemo.controller;

import com.coderscantabria.streamsdemo.models.OrderCountDTO;
import com.coderscantabria.streamsdemo.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class StreamsDemoController {

    private final OrderService orderService;

    @GetMapping("/count")
    public ResponseEntity<List<OrderCountDTO>> countOrders() {

        return ResponseEntity.ok(orderService.ordersCount());
    }

    @GetMapping("/count/{productId}")
    public ResponseEntity<OrderCountDTO> countOrder(@PathVariable String productId) {

        var result = orderService.getOrderCountById(productId);
        return result.isPresent()? ResponseEntity.ok(result.get()) : ResponseEntity.notFound().build();
    }
}
