package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.dtos.ApiExterna.DisponibilidadDTO;
import ar.edu.utn.frc.tup.lciv.dtos.ApiExterna.PrecioDTO;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.POSTReserva;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.Reserva;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.ReservaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
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

    DisponibilidadDTO disponibilidad;
    PrecioDTO precio;

    @BeforeEach
    void setUp() {
        disponibilidad = new DisponibilidadDTO();
        disponibilidad.setFecha("2024-10-21");
        disponibilidad.setHotel_id(123L);
        disponibilidad.setTipo_habitacion("SIMPLE");
        disponibilidad.setDisponible(true);
        precio = new PrecioDTO();
        precio.setId_hotel(123L);
        precio.setTipo_habitacion("SIMPLE");
        precio.setPrecio(BigDecimal.valueOf(5165));

    }

    @Test
    void getDisponibilidad() {
        // Preparar datos mock
        Map<String, Object> mockResponse = createDispMap(123L, "SIMPLE", "2024-10-21", true);

        // Configurar el mock del RestTemplate
        when(restTemplate.getForObject(
                eq("http://localhost:8080/habitacion/disponibilidad?hotel_id=123&tipo_habitacion=SIMPLE&fecha=2024-10-21"),
                eq(Map.class)
        )).thenReturn(mockResponse);

        // Ejecutar el método bajo prueba
        DisponibilidadDTO resultado = restClient.getDisponibilidad(123L, "SIMPLE", "2024-10-21");

        // Verificar la llamada al RestTemplate
        verify(restTemplate).getForObject(
                eq("http://localhost:8080/habitacion/disponibilidad?hotel_id=123&tipo_habitacion=SIMPLE&fecha=2024-10-21"),
                eq(Map.class)
        );

        // Verificar los resultados
        assertNotNull(resultado);
        assertEquals(123L, resultado.getHotel_id());
        assertEquals("SIMPLE", resultado.getTipo_habitacion());
        assertEquals("2024-10-21", resultado.getFecha());
        assertTrue(resultado.getDisponible());
    }

    // Método helper para crear el Map de respuesta mock
    private Map<String, Object> createDispMap(Long hotelId, String tipoHabitacion, String fecha, Boolean disponible) {
        Map<String, Object> disponibilidad = new HashMap<>();
        disponibilidad.put("hotel_id", hotelId);
        disponibilidad.put("tipo_habitacion", tipoHabitacion);
        disponibilidad.put("fecha", fecha);
        disponibilidad.put("disponible", disponible);
        return disponibilidad;
    }

    @Test
    void getDisponibilidad_NoResponse() {
        // Configurar el mock del RestTemplate para que devuelva null
        when(restTemplate.getForObject(
                eq("http://localhost:8080/habitacion/disponibilidad?hotel_id=123&tipo_habitacion=SIMPLE&fecha=2024-10-21"),
                eq(Map.class)
        )).thenReturn(null);

        // Ejecutar el método bajo prueba y verificar que lanza la excepción
        assertThrows(IllegalArgumentException.class, () -> {
            restClient.getDisponibilidad(123L, "SIMPLE", "2024-10-21");
        });

        // Verificar que el RestTemplate fue llamado correctamente
        verify(restTemplate).getForObject(
                eq("http://localhost:8080/habitacion/disponibilidad?hotel_id=123&tipo_habitacion=SIMPLE&fecha=2024-10-21"),
                eq(Map.class)
        );
    }

    @Test
    void getPrecio() {
        // Preparar datos mock
        Map<String, Object> mockResponse = createPrecioMap(123L, "SIMPLE", new BigDecimal("5035.87"));

        // Configurar el mock del RestTemplate
        when(restTemplate.getForObject(
                eq("http://localhost:8080/habitacion/precio?hotel_id=123&tipo_habitacion=SIMPLE"),
                eq(Map.class)
        )).thenReturn(mockResponse);

        // Ejecutar el método bajo prueba
        PrecioDTO resultado = restClient.getPrecio(123L, "SIMPLE");

        // Verificar la llamada al RestTemplate
        verify(restTemplate).getForObject(
                eq("http://localhost:8080/habitacion/precio?hotel_id=123&tipo_habitacion=SIMPLE"),
                eq(Map.class)
        );

        // Verificar los resultados
        assertNotNull(resultado);
        assertEquals(123L, resultado.getId_hotel());
        assertEquals("SIMPLE", resultado.getTipo_habitacion());
        assertEquals(new BigDecimal("5035.87"), resultado.getPrecio());
    }

    // Método helper para crear el Map de respuesta mock
    private Map<String, Object> createPrecioMap(Long hotelId, String tipoHabitacion, BigDecimal precio) {
        Map<String, Object> precioData = new HashMap<>();
        precioData.put("id_hotel", hotelId);
        precioData.put("tipo_habitacion", tipoHabitacion);
        precioData.put("precio_lista", precio.toString());
        return precioData;
    }

    @Test
    void getPrecio_NoResponse() {
        when(restTemplate.getForObject(
                eq("http://localhost:8080/habitacion/precio?hotel_id=123&tipo_habitacion=SIMPLE"),
                eq(Map.class)
        )).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            restClient.getPrecio(123L, "SIMPLE");
        });

        verify(restTemplate).getForObject(
                eq("http://localhost:8080/habitacion/precio?hotel_id=123&tipo_habitacion=SIMPLE"),
                eq(Map.class)
        );
    }

}