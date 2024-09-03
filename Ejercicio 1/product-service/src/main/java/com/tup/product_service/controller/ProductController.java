package com.tup.product_service.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @GetMapping
    public String getProducts() {
        return "List of products";
    }
}