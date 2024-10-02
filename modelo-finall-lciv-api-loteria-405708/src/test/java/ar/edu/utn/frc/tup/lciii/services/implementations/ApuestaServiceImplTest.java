package ar.edu.utn.frc.tup.lciii.services.implementations;

import ar.edu.utn.frc.tup.lciii.dtos.common.EndpointSorteoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ApuestaServiceImplTest {

    @MockBean
    private RestTemplate restTemplate;
    @InjectMocks
    private ApuestaServiceImpl apuestaService;
    private EndpointSorteoDto[] mockSorteos;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        List<List<Integer>> numerosSorteados = new ArrayList<>();
        mockSorteos = new EndpointSorteoDto[]{
                new EndpointSorteoDto(123, "2024-01-16", 1000, numerosSorteados)
        };

    }

    @Test
    void obtenerSorteos(){
        when(restTemplate.getForEntity(
                eq("http://localhost:8082/sorteos?fecha=2024-01-16"),
                eq(EndpointSorteoDto[].class)
        )).thenReturn(new ResponseEntity<>(mockSorteos, HttpStatus.OK));

        //Dividir el metodo ObtenerSorteos, para poder testearlo
        EndpointSorteoDto sorteo = apuestaService.obtenerSorteo("2024-01-16");

        assertNotNull(sorteo);
        assertEquals(123, sorteo.getNumeroSorteo());
        assertEquals("2024-01-16", sorteo.getFecha());
        assertEquals(1000, sorteo.getDineroTotalAcumulado());
    }

}