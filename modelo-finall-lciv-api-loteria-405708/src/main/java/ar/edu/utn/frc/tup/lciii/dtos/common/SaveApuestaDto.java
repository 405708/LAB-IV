package ar.edu.utn.frc.tup.lciii.dtos.common;

import ar.edu.utn.frc.tup.lciii.domains.Resultado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveApuestaDto {
    private Integer id_sorteo;
    private String fecha_sorteo;
    private String id_cliente;
    private String numero;
    private Resultado resultado;
}
