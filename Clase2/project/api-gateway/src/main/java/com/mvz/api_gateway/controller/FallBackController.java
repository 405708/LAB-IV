package com.mvz.api_gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallBackController {

    @GetMapping("/product")
    public String productFallback(){
        return "Servicio no disponible";
    }

    @GetMapping("/order")
    public String orderFallback(){
        return "Servicio no disponible";
    }

    @GetMapping("/user")
    public String userFallback(){
        return "Servicio no disponible";
    }
}
