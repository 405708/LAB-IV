package ar.edu.utn.frc.tup.lc.iv.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultadoEndpointDTO {
    private Long id;
    private Long distritoId;
    private Long cargoId;
    private Long seccionId;
    private Long agrupacionId;
    private String votosTipo;
    private Integer votosCantidad;
}
