package ar.edu.utn.frc.tup.lciv.dtos.habitacion;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class POSTReserva {

    @JsonProperty("id_hotel")
    private Long idHotel;

    @JsonProperty("id_cliente")
    private Long idCliente;

    @JsonProperty("tipo_habitacion")
    private String tipoHabitacion;

    @JsonProperty("fecha_ingreso")
    private LocalDateTime fechaIngreso;

    @JsonProperty("fecha_salida")
    private LocalDateTime fechaSalida;

    @JsonProperty("medio_pago")
    private String medioPago;
}
