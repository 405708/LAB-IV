package ar.edu.utn.frc.tup.lciii.dtos.common;

import ar.edu.utn.frc.tup.lciii.domains.Resultado;
import lombok.Data;

@Data
public class SaveApuestaDto {
    private Integer id_sorteo;
    private String fecha_sorteo;
    private String id_cliente;
    private String numero;
    private Resultado resultado;
}
