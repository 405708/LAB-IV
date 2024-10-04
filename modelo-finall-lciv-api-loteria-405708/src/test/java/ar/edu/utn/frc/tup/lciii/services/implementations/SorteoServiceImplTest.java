package ar.edu.utn.frc.tup.lciii.services.implementations;

import ar.edu.utn.frc.tup.lciii.domains.Apuesta;
import ar.edu.utn.frc.tup.lciii.domains.Resultado;
import ar.edu.utn.frc.tup.lciii.dtos.common.ApuestaSorteoDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.EndpointSorteoDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.SorteoBetsDto;
import ar.edu.utn.frc.tup.lciii.repositories.ApuestaRepository;
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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class SorteoServiceImplTest {

    @MockBean
    private RestTemplate restTemplate;

    @InjectMocks
    private SorteoServiceImpl sorteoService;

    @Mock
    private ApuestaRepository apuestaRepository;

    private EndpointSorteoDto[] mockSorteos; // Cambiado a EndpointSorteoDto
    private SorteoBetsDto sorteoBetsDto;

    private final List<ApuestaSorteoDto> EMPTY_APUESTAS = new ArrayList<>();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockSorteos = new EndpointSorteoDto[]{
                new EndpointSorteoDto(123, "2024-01-16", 1000, new ArrayList<>())
        };
        sorteoBetsDto = new SorteoBetsDto();
        sorteoBetsDto.setFecha_sorteo("2024-01-16");
        sorteoBetsDto.setTotalEnReserva(1000);
        sorteoBetsDto.setId_sorteo(123);
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

    @Test
    void GetSorteoById_success() {
        Integer idToFind = 123;

        // Simula el comportamiento de obtener los sorteos
        when(restTemplate.getForEntity(any(String.class), eq(EndpointSorteoDto[].class)))
                .thenReturn(new ResponseEntity<>(mockSorteos, HttpStatus.OK));

        // Llama al método que deseas probar
        SorteoBetsDto result = sorteoService.getByIdWithBet(idToFind);

        // Verifica el resultado
        assertThat(result).isNotNull();
        assertThat(result.getId_sorteo()).isEqualTo(123);
        assertThat(result.getFecha_sorteo()).isEqualTo("2024-01-16");
        assertThat(result.getTotalEnReserva()).isEqualTo(1000);
    }

    @Test
    void GetSorteoById_Is_Null() {
        // Llama al método que deseas probar con un valor null
        SorteoBetsDto result = sorteoService.getByIdWithBet(null);

        // Verifica que el resultado sea null
        assertNull(result);

        // Verifica que se haya llamado al método con el valor null
        verify(apuestaRepository, times(0)).getApuestasByFechaSorteo(anyString()); // Asegúrate de que no se llamara al repositorio
    }

    @Test
    void getSorteoSuccess_Not_Apuestas(){

        Integer idToFind = 123;

        // Simula el comportamiento de obtener los sorteos
        when(restTemplate.getForEntity(any(String.class), eq(EndpointSorteoDto[].class)))
                .thenReturn(new ResponseEntity<>(mockSorteos, HttpStatus.OK));

        // Llama al método que deseas probar
        SorteoBetsDto result = sorteoService.getByIdWithBet(idToFind);

        // Verifica el resultado
        assertThat(result).isNotNull();
        assertThat(result.getId_sorteo()).isEqualTo(123);
        assertThat(result.getFecha_sorteo()).isEqualTo("2024-01-16");
        assertThat(result.getTotalEnReserva()).isEqualTo(1000);
        assertTrue(result.getApuestas().isEmpty());
    }




}