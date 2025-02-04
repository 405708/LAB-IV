package ar.edu.utn.frc.tup.lciv.dtos.ApiExterna;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrecioDTO {
    public Long id_hotel;
    public String tipo_habitacion;
    public BigDecimal precio;
}
