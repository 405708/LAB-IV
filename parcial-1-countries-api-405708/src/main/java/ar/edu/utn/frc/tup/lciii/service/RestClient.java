package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.model.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestClient {
    @Autowired
    private RestTemplate restTemplate;

    String url = "https://restcountries.com/v3.1/all";

    public List<Country> getAllCountries() {
        List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
        return response.stream().map(this::mapToCountry).collect(Collectors.toList());
    }


    /**
     * Agregar mapeo de campo cca3 (String)
     * Agregar mapeo campos borders ((List<String>))
     */
    private Country mapToCountry(Map<String, Object> countryData) {
        Map<String, Object> nameData = (Map<String, Object>) countryData.get("name");

        // Obtener los campos opcionales "borders" y "languages" con comprobaciones de existencia
        List<String> borders = countryData.containsKey("borders")
                ? (List<String>) countryData.get("borders")
                : new ArrayList<>();  // Si no tiene "borders", devolver una lista vacía

        Map<String, String> languages = countryData.containsKey("languages")
                ? (Map<String, String>) countryData.get("languages")
                : new HashMap<>();  // Si no tiene "languages", devolver un mapa vacío


        return Country.builder()
                .name((String) nameData.get("common"))
                .code((String) countryData.get("cca3"))
                .population(((Number) countryData.getOrDefault("population", 0)).longValue()) // valor predeterminado
                .area(((Number) countryData.getOrDefault("area", 0.0)).doubleValue()) // valor predeterminado
                .region((String) countryData.getOrDefault("region", "N/A")) // valor predeterminado
                .languages(languages)
                .borders(borders) // Manejo opcional
                .build();
    }

}
