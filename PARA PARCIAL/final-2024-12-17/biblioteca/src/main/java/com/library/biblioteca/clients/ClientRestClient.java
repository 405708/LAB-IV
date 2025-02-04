package com.library.biblioteca.clients;

import com.library.biblioteca.dto.ClienteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClientRestClient {

    @Autowired
    private RestTemplate restTemplate;

    private String url = "http://localhost:8081";

    public ClienteDTO getRandomClient() {
        return restTemplate.getForObject(url + "/api/personas/aleatorio", ClienteDTO.class);
    }

}
