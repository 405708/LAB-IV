package com.tup.order_service.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @GetMapping
    public String getOrders() {
        return "List of orders";
    }
}