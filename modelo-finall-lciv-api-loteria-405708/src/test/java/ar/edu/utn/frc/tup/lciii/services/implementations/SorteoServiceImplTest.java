package ar.edu.utn.frc.tup.lciii.services.implementations;

import ar.edu.utn.frc.tup.lciii.dtos.common.EndpointSorteoDto;
import net.bytebuddy.TypeCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class SorteoServiceImplTest {

    @MockBean
    private RestTemplate restTemplate;

    @InjectMocks
    private SorteoServiceImpl sorteoService;

    private EndpointSorteoDto[] mockSorteos; // Cambiado a EndpointSorteoDto

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockSorteos = new EndpointSorteoDto[]{
                new EndpointSorteoDto(123, "2024-01-16", 1000, new ArrayList<>())
        };
    }

    @Test
    void obtenerSorteos() {
        // Configurar el mock
        when(restTemplate.getForEntity(
                eq("http://localhost:8082/sorteos"),
                eq(EndpointSorteoDto[].class)
        )).thenReturn(new ResponseEntity<>(mockSorteos, HttpStatus.OK));

        // Ejecutar el método
        List<EndpointSorteoDto> sorteos = sorteoService.obtenerSorteos();

        // Verificar que el mock fue llamado
        verify(restTemplate).getForEntity(
                eq("http://localhost:8082/sorteos"),
                eq(EndpointSorteoDto[].class)
        );

        // Aserciones
        assertNotNull(sorteos);
        assertEquals(1, sorteos.size());
        assertEquals(123, sorteos.get(0).getNumeroSorteo());
        assertEquals("2024-01-16", sorteos.get(0).getFecha());
        assertEquals(1000, sorteos.get(0).getDineroTotalAcumulado());
    }
}