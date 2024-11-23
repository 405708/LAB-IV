package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.dtos.CargoDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.DistritoDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.SeccionDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class RestClientTest {

    @MockBean
    private RestTemplate restTemplate;

    @InjectMocks
    private RestClient restClient;

    @Test
    void getAllDistritos_WithoutName_ShouldReturnAllDistritos() {
        // Preparar datos mock
        List<Map<String, Object>> mockResponse = Arrays.asList(
                createDistritoMap(1L, "Buenos Aires"),
                createDistritoMap(2L, "Córdoba")
        );

        // Configurar el mock
        when(restTemplate.getForObject(
                eq("http://localhost:8080/distritos"),
                eq(List.class)
        )).thenReturn(mockResponse);

        // Ejecutar
        List<DistritoDTO> resultado = restClient.getAllDistritos(null);

        // Verificar
        verify(restTemplate).getForObject(
                eq("http://localhost:8080/distritos"),
                eq(List.class)
        );

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Buenos Aires", resultado.get(0).getNombre());
        assertEquals(2L, resultado.get(1).getId());
        assertEquals("Córdoba", resultado.get(1).getNombre());
    }

    @Test
    void getAllDistritos_WithName_ShouldReturnFilteredDistritos() {
        // Preparar datos mock
        List<Map<String, Object>> mockResponse = Arrays.asList(
                createDistritoMap(1L, "Buenos Aires")
        );

        // Configurar el mock
        when(restTemplate.getForObject(
                eq("http://localhost:8080/distritos?distritoNombre=Buenos Aires"),
                eq(List.class)
        )).thenReturn(mockResponse);

        // Ejecutar
        List<DistritoDTO> resultado = restClient.getAllDistritos("Buenos Aires");

        // Verificar
        verify(restTemplate).getForObject(
                eq("http://localhost:8080/distritos?distritoNombre=Buenos Aires"),
                eq(List.class)
        );

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Buenos Aires", resultado.get(0).getNombre());
    }

    @Test
    void getDistritoById_ExistingId_ShouldReturnDistrito() {
        // Preparar datos mock
        List<Map<String, Object>> mockResponse = Arrays.asList(
                createDistritoMap(1L, "Buenos Aires")
        );

        // Configurar el mock
        when(restTemplate.getForObject(
                eq("http://localhost:8080/distritos?distritoId=1"),
                eq(List.class)
        )).thenReturn(mockResponse);

        // Ejecutar
        DistritoDTO resultado = restClient.getDistritoById(1L);

        // Verificar
        verify(restTemplate).getForObject(
                eq("http://localhost:8080/distritos?distritoId=1"),
                eq(List.class)
        );

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Buenos Aires", resultado.getNombre());
    }

    @Test
    void getDistritoById_NonExistingId_ShouldThrowException() {
        // Configurar el mock para devolver lista vacía
        when(restTemplate.getForObject(
                eq("http://localhost:8080/distritos?distritoId=999"),
                eq(List.class)
        )).thenReturn(Arrays.asList());

        // Verificar que se lanza la excepción
        assertThrows(IllegalArgumentException.class, () -> {
            restClient.getDistritoById(999L);
        });
    }

    // Método helper para crear el Map de respuesta mock
    private Map<String, Object> createDistritoMap(Long id, String nombre) {
        Map<String, Object> distrito = new HashMap<>();
        distrito.put("distritoId", id);
        distrito.put("distritoNombre", nombre);
        return distrito;
    }

    @Test
    void getCargosById() {
        // Preparar datos mock
        List<Map<String, Object>> mockResponse = Arrays.asList(
                createCargoMap(1L, "Presi"),
                createCargoMap(2L, "Vice")
        );

        // Configurar el mock
        when(restTemplate.getForObject(
                eq("http://localhost:8080/cargos?distritoId=1"),
                eq(List.class)
        )).thenReturn(mockResponse);

        // Ejecutar
        List<CargoDTO> resultado = restClient.getCargosById(1L);

        // Verificar
        verify(restTemplate).getForObject(
                eq("http://localhost:8080/cargos?distritoId=1"),
                eq(List.class)
        );

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Presi", resultado.get(0).getNombre());
        assertEquals(2L, resultado.get(1).getId());
        assertEquals("Vice", resultado.get(1).getNombre());
    }


    @Test
    void getCargosByIdAndCargoId() {
        // Preparar datos mock
        List<Map<String, Object>> mockResponse = Arrays.asList(
                createCargoMap(1L, "Presi")
        );

        // Configurar el mock
        when(restTemplate.getForObject(
                eq("http://localhost:8080/cargos?distritoId=1&cargoId=1"),
                eq(List.class)
        )).thenReturn(mockResponse);

        // Ejecutar
        CargoDTO resultado = restClient.getCargosByIdAndCargoId(1L, 1L);

        // Verificar
        verify(restTemplate).getForObject(
                eq("http://localhost:8080/cargos?distritoId=1&cargoId=1"),
                eq(List.class)
        );

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Presi", resultado.getNombre());
    }

    // Método helper para crear el Map de respuesta mock
    private Map<String, Object> createCargoMap(Long id, String nombre) {
        Map<String, Object> distrito = new HashMap<>();
        distrito.put("cargoId", id);
        distrito.put("cargoNombre", nombre);
        return distrito;
    }



    @Test
    void getSeccionById() {
        // Preparar datos mock
        List<Map<String, Object>> mockResponse = Arrays.asList(
                createSeccionMap(1L, "Chacabuco"),
                createSeccionMap(2L, "Juk")
        );

        // Configurar el mock
        when(restTemplate.getForObject(
                eq("http://localhost:8080/secciones?distritoId=1"),
                eq(List.class)
        )).thenReturn(mockResponse);

        // Ejecutar
        List<SeccionDTO> resultado = restClient.getSeccionById(1L);

        // Verificar
        verify(restTemplate).getForObject(
                eq("http://localhost:8080/secciones?distritoId=1"),
                eq(List.class)
        );

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Chacabuco", resultado.get(0).getNombre());
        assertEquals(2L, resultado.get(1).getId());
        assertEquals("Juk", resultado.get(1).getNombre());
    }

    @Test
    void getSeccionByIdAndDistritoId() {
        // Preparar datos mock
        List<Map<String, Object>> mockResponse = Arrays.asList(
                createSeccionMap(1L, "Chacabuco")
        );

        // Configurar el mock
        when(restTemplate.getForObject(
                eq("http://localhost:8080/secciones?distritoId=1&seccionId=1"),
                eq(List.class)
        )).thenReturn(mockResponse);

        // Ejecutar
        SeccionDTO resultado = restClient.getSeccionByIdAndDistritoId(1L, 1L);

        // Verificar
        verify(restTemplate).getForObject(
                eq("http://localhost:8080/secciones?distritoId=1&seccionId=1"),
                eq(List.class)
        );

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Chacabuco", resultado.getNombre());
    }

    // Método helper para crear el Map de respuesta mock
    private Map<String, Object> createSeccionMap(Long id, String nombre) {
        Map<String, Object> distrito = new HashMap<>();
        distrito.put("seccionId", id);
        distrito.put("seccionNombre", nombre);
        return distrito;
    }



    @Test
    void getResultadosByDistritoId() {
    }

    @Test
    void mapToResDTO() {
    }



    @Test
    void getAgrupacionById() {
    }

    @Test
    void mapToAgrupacionDTO() {
    }
}