package ar.edu.utn.frc.tup.lciii.services.implementations;

import ar.edu.utn.frc.tup.lciii.domains.Apuesta;
import ar.edu.utn.frc.tup.lciii.domains.Resultado;
import ar.edu.utn.frc.tup.lciii.dtos.common.ApuestaDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.EndpointSorteoDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.SaveApuestaDto;
import ar.edu.utn.frc.tup.lciii.repositories.ApuestaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ApuestaServiceImplTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private ApuestaServiceImpl apuestaService;

    @MockBean
    private ApuestaRepository apuestaRepository;

    @MockBean(name = "modelMapper")
    private ModelMapper modelMapper;

    private EndpointSorteoDto[] mockSorteos;
    private ApuestaDto apuestaDto;
    private Apuesta apuesta;
    private SaveApuestaDto savedApuestaDto;
    private SaveApuestaDto savedApuestaDto2;

    @BeforeEach
    void setUp() {
        List<Integer> nro = new ArrayList<>();
        nro.add(45678);
        nro.add(12345);

        List<List<Integer>> numerosSorteados = new ArrayList<>();
        numerosSorteados.add(nro);

        mockSorteos = new EndpointSorteoDto[]{
                new EndpointSorteoDto(123, "2024-01-16", 100000, numerosSorteados)
        };

        apuesta = new Apuesta();
        apuesta.setResultado(Resultado.GANADOR);
        apuesta.setFecha_sorteo("2024-01-16");
        apuesta.setId_cliente("Pedro");
        apuesta.setMontoApostado(10);

        apuestaDto = new ApuestaDto();
        apuestaDto.setFecha_sorteo("2024-01-16");
        apuestaDto.setId_cliente("Pedro");
        apuestaDto.setMontoApostado(10);
        apuestaDto.setNumero("123");

        savedApuestaDto = new SaveApuestaDto();
        savedApuestaDto.setResultado(Resultado.GANADOR);
        savedApuestaDto.setId_sorteo(mockSorteos[0].getNumeroSorteo());

        savedApuestaDto2 = new SaveApuestaDto();
        savedApuestaDto2.setResultado(Resultado.PERDEDOR);
        savedApuestaDto2.setId_sorteo(mockSorteos[0].getNumeroSorteo());
    }

    @Test
    void PostApuestaGANADOR() {
        // Configurar el mock de RestTemplate
        ResponseEntity<EndpointSorteoDto[]> responseEntity = new ResponseEntity<>(mockSorteos, HttpStatus.OK);

        when(restTemplate.getForEntity(
                contains("/sorteos?fecha=2024-01-16"),
                eq(EndpointSorteoDto[].class)
        )).thenReturn(responseEntity);

        // Mock para el guardado en el repositorio
        when(apuestaRepository.save(any(Apuesta.class)))
                .thenReturn(apuesta);

        // Mock para convertir Apuesta a SaveApuestaDto
        when(modelMapper.map(any(Apuesta.class), eq(SaveApuestaDto.class)))
                .thenReturn(savedApuestaDto);

        // Ejecutar el método bajo prueba
        SaveApuestaDto result = apuestaService.save(apuestaDto);

        // Verificaciones
        assertNotNull(result);
        assertEquals(Resultado.GANADOR, result.getResultado());
        assertEquals(mockSorteos[0].getNumeroSorteo(), result.getId_sorteo());

        // Verificar que los mocks se usaron correctamente
        verify(restTemplate).getForEntity(
                contains("/sorteos?fecha=2024-01-16"),
                eq(EndpointSorteoDto[].class)
        );
        verify(apuestaRepository).save(any(Apuesta.class));
        verify(modelMapper).map(any(Apuesta.class), eq(SaveApuestaDto.class));
    }


    @Test
    void PostApuestaPERDEDOR(){

        // Configurar el mock de RestTemplate
        ResponseEntity<EndpointSorteoDto[]> responseEntity = new ResponseEntity<>(mockSorteos, HttpStatus.OK);

        when(restTemplate.getForEntity(
                contains("/sorteos?fecha=2024-01-16"),
                eq(EndpointSorteoDto[].class)
        )).thenReturn(responseEntity);

        // Mock para el guardado en el repositorio
        when(apuestaRepository.save(any(Apuesta.class)))
                .thenReturn(apuesta);

        // Mock para convertir Apuesta a SaveApuestaDto
        when(modelMapper.map(any(Apuesta.class), eq(SaveApuestaDto.class)))
                .thenReturn(savedApuestaDto2);


        // Ejecutar el método bajo prueba
        SaveApuestaDto result = apuestaService.save(apuestaDto);

        // Verificaciones
        assertNotNull(result);
        assertEquals(Resultado.PERDEDOR, result.getResultado());
        assertEquals(mockSorteos[0].getNumeroSorteo(), result.getId_sorteo());

        // Verificar que los mocks se usaron correctamente
        verify(restTemplate).getForEntity(
                contains("/sorteos?fecha=2024-01-16"),
                eq(EndpointSorteoDto[].class)
        );
        verify(apuestaRepository).save(any(Apuesta.class));
        verify(modelMapper).map(any(Apuesta.class), eq(SaveApuestaDto.class));
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
        assertEquals(100000, sorteo.getDineroTotalAcumulado());
    }
}