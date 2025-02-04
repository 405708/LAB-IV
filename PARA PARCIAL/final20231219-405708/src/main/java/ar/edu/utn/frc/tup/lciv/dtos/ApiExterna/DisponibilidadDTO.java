package ar.edu.utn.frc.tup.lciv.dtos.ApiExterna;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisponibilidadDTO {
    public String tipo_habitacion;
    public Long hotel_id;
    public String fecha;
    public Boolean disponible;
}
