package ar.edu.utn.frc.tup.lciv.dtos.habitacion;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "id_hotel")
    private Long idHotel;

    @Column(name = "tipo_habitacion")
    private String tipoHabitacion;

    @Column(name = "fecha_ingreso")
    private LocalDateTime fechaIngreso;

    @Column(name = "fecha_salida")
    private LocalDateTime fechaSalida;

    @Column(name = "estado_reserva")
    private String estadoReserva;

    @Column(name = "medio_pago")
    private String medioPago;

    @Column(name = "precio")
    private BigDecimal precio;

}
