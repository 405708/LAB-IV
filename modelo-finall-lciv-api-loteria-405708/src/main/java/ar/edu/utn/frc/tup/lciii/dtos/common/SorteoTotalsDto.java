package ar.edu.utn.frc.tup.lciii.dtos.common;

import lombok.Data;

@Data
public class SorteoTotalsDto {
    private Integer id_sorteo;
    private String fecha_sorteo;
    private Integer totalDeApuestas;
    private Integer totalPagado;
    private Integer totalEnReserva;
}
