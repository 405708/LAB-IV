package ar.edu.utn.frc.tup.lciv.controllers;

import ar.edu.utn.frc.tup.lciv.dtos.ApiExterna.DisponibilidadDTO;
import ar.edu.utn.frc.tup.lciv.dtos.ApiExterna.PrecioDTO;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.POSTReserva;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.ReservaDTO;
import ar.edu.utn.frc.tup.lciv.services.ReservaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebMvcTest(ReservaController.class)
@AutoConfigureMockMvc
class ReservaControllerTest {

    private static final String RESERVA_URL = "/reserva";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReservaService reservaService;
    @Autowired
    private ObjectMapper objectMapper;

    DisponibilidadDTO disponibilidad;
    PrecioDTO precio;
    ReservaDTO reserva;
    ReservaDTO reservaDTO;
    POSTReserva postReserva;

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
        reserva = new ReservaDTO();
        reserva.setIdReserva(1L);
        reserva.setIdCliente(456L);
        reserva.setIdHotel(123L);
        reserva.setTipoHabitacion("SIMPLE");
//        reserva.setEstadoReserva("EXITOSA");
        reserva.setMedioPago("EFECTIVO");
//        reserva.setPrecio(BigDecimal.valueOf(5035.875));


        reservaDTO = new ReservaDTO(1L, 456L, 123L, "SIMPLE",
                LocalDateTime.of(2024, 10, 21, 14, 0, 0),
                LocalDateTime.of(2024, 10, 22, 12, 0, 0),
                "EXITOSA", "EFECTIVO", BigDecimal.valueOf(5035.875));
        postReserva = new POSTReserva(123L, 456L, "SIMPLE",
                LocalDateTime.of(2024, 10, 21, 14, 0, 0),
                LocalDateTime.of(2024, 10, 22, 12, 0, 0), "EFECTIVO");


    }


    @Test
    void getReserva() throws Exception {
        Long idToFind = 1L;

        when(reservaService.getReserva(idToFind)).thenReturn(reserva);

        MockHttpServletResponse response = this.mockMvc.perform(get(RESERVA_URL + "/" + idToFind) // ID en la URL
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(asJsonString(reserva));
    }

    @Test
    void getReserva_NotFound() throws Exception {
        Long idToFind = 99L;

        when(reservaService.getReserva(idToFind)).thenReturn(null);

        MockHttpServletResponse response = this.mockMvc.perform(get(RESERVA_URL + "/" + idToFind) // ID en la URL
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    // Test para crear una reserva con éxito
    @Test
    void createReserva() throws Exception {
        // Cuando el servicio devuelve una reserva válida
        when(reservaService.createReserva(any(POSTReserva.class))).thenReturn(reservaDTO);

        // Realizamos el POST
        MockHttpServletResponse response = this.mockMvc.perform(post(RESERVA_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postReserva))) // Enviamos el DTO como JSON
                .andReturn().getResponse();

        // Validamos la respuesta
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(reservaDTO));
    }

    // Test para cuando la reserva no se puede crear (por ejemplo, servicio devuelve null)
    @Test
    void createReserva_NotFound() throws Exception {
        // Cuando el servicio no puede crear la reserva (por alguna razón)
        when(reservaService.createReserva(any(POSTReserva.class))).thenReturn(null);

        // Realizamos el POST
        MockHttpServletResponse response = this.mockMvc.perform(post(RESERVA_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postReserva))) // Enviamos el DTO como JSON
                .andReturn().getResponse();

        // Validamos la respuesta
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }


    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}