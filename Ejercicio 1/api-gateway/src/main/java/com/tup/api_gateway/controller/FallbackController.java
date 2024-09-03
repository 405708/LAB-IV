package com.tup.api_gateway.controller;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/product")
    public String productFallback() {
        return "Product service is not available at the moment. Please try again later.";
    }

    @GetMapping("/order")
    public String orderFallback() {
        return "Order service is not available at the moment. Please try again later.";
    }

    @GetMapping("/user")
    public String userFallback() {
        return "User service is not available at the moment. Please try again later.";
    }
}