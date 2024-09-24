package ar.edu.utn.frc.tup.lciii.dtos.common;

import lombok.Data;

import java.util.List;

@Data
public class SorteoBetsDto {
    private Integer id_sorteo;
    private String fecha_sorteo;
    private List<ApuestaSorteoDto>  apuestas;
    private Integer totalEnReserva;
}
