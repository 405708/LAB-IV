package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.common.EndpointTeamsDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RestClient {
    @Autowired
    private RestTemplate restTemplate;

    private static final String RESILIENCE4J_INSTANCE_NAME = "circuitBreakerParcial";
    private static final String FALLBACK_METHOD = "fallback";

    public ResponseEntity<String> fallback(Exception ex) {
        return ResponseEntity.status(503).body("Response from Circuit Breaker.");
    }

    private String url = "https://my-json-server.typicode.com/LCIV-2023/fake-api-rwc2023/teams";

    @CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME, fallbackMethod = FALLBACK_METHOD)
    public ResponseEntity<EndpointTeamsDto> getTeamById(Integer id){
        EndpointTeamsDto teams = restTemplate.getForObject(url + "/" + id, EndpointTeamsDto.class);
        return ResponseEntity.ok(teams);
    }

    @CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME, fallbackMethod = FALLBACK_METHOD)
    public ResponseEntity<List<EndpointTeamsDto>> getTeamsByCountry(String country){
        ResponseEntity<List<EndpointTeamsDto>> teams = ResponseEntity.ok(Arrays.asList(restTemplate.getForObject(url, EndpointTeamsDto[].class)));

        List<EndpointTeamsDto> teamsForFilter = teams.getBody();

        List<EndpointTeamsDto> teamsToShow = new ArrayList<>();

        for (EndpointTeamsDto team : teamsForFilter){
            if(team.getCountry().equals(country)){
                teamsToShow.add(team);
            }
        }
        return ResponseEntity.ok(teamsToShow);
    }

}
