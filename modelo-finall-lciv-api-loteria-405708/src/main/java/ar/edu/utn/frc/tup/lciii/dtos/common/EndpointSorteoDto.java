package ar.edu.utn.frc.tup.lciii.dtos.common;

import ar.edu.utn.frc.tup.lciii.domains.NumeroSorteado;
import lombok.Data;

import java.util.List;

@Data
public class EndpointSorteoDto {
    private Integer id_sorteo;
    private String fecha_sorteo;
    private Integer totalEnReserva;
    private List<NumeroSorteado> numerosSorteados;
}
