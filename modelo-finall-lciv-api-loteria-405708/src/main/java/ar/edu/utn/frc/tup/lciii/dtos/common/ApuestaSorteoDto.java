package ar.edu.utn.frc.tup.lciii.dtos.common;

import ar.edu.utn.frc.tup.lciii.domains.Resultado;
import lombok.Data;

@Data
public class ApuestaSorteoDto {
    private String id_cliente;
    private String numero;
    private Resultado resultado;
    private Integer montoApostado;
    private Integer premio;
}
