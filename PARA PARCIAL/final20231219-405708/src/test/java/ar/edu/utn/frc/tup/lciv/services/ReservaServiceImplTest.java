package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.dtos.ApiExterna.DisponibilidadDTO;
import ar.edu.utn.frc.tup.lciv.dtos.ApiExterna.PrecioDTO;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.POSTReserva;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.Reserva;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.ReservaDTO;
import ar.edu.utn.frc.tup.lciv.repositories.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ReservaServiceImplTest {

    @InjectMocks
    private ReservaServiceImpl reservaService;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    RestClient restClient;

    DisponibilidadDTO disponibilidad;
    PrecioDTO precio;
    ReservaDTO reservaDTO;
    POSTReserva postReserva;
    Reserva reserva;

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

        reservaDTO = new ReservaDTO(1L, 456L, 123L, "SIMPLE",
                LocalDateTime.of(2025, 02, 21, 14, 0, 0),
                LocalDateTime.of(2025, 02, 22, 12, 0, 0),
                null, "EFECTIVO", null);
        postReserva = new POSTReserva(123L, 456L, "SIMPLE",
                LocalDateTime.of(2025, 02, 21, 14, 0, 0),
                LocalDateTime.of(2025, 02, 22, 12, 0, 0), "EFECTIVO");

        reserva = new Reserva();
        reserva.setId(1L);
        reserva.setIdCliente(456L);
        reserva.setIdHotel(123L);
        reserva.setTipoHabitacion("SIMPLE");
        reserva.setFechaIngreso(LocalDateTime.of(2025, 02, 21, 14, 0, 0));  // Fecha de ingreso
        reserva.setFechaSalida(LocalDateTime.of(2025, 02, 22, 12, 0, 0));  // Fecha de salida
        reserva.setEstadoReserva("EXITOSA");
        reserva.setMedioPago("EFECTIVO");
        reserva.setPrecio(BigDecimal.valueOf(5035.875));

    }

    @Test
    void createReserva() {
        when(restClient.getDisponibilidad(123L, "SIMPLE", "2025-02-21")).thenReturn(disponibilidad);
        when(restClient.getPrecio(123L, "SIMPLE")).thenReturn(precio);
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        ReservaDTO reservaDTO = reservaService.createReserva(postReserva);
        assertNotNull(reservaDTO);
        assertEquals(reservaDTO.getIdReserva(), reserva.getId());
        assertEquals(reservaDTO.getPrecio(), BigDecimal.valueOf(5035.875).setScale(4, RoundingMode.HALF_UP));
        assertEquals("EXITOSA", reservaDTO.getEstadoReserva());
        assertEquals("EFECTIVO", reservaDTO.getMedioPago());

    }


    @Test
    void getReserva() {
        when(reservaRepository.getReservaById(1L)).thenReturn(reserva);
        ReservaDTO reservaDTO = reservaService.getReserva(1L);
        assertNotNull(reservaDTO);
        assertEquals(reservaDTO.getIdReserva(), reserva.getId());
        assertEquals("EXITOSA", reservaDTO.getEstadoReserva());
        assertEquals("EFECTIVO", reservaDTO.getMedioPago());
    }
}