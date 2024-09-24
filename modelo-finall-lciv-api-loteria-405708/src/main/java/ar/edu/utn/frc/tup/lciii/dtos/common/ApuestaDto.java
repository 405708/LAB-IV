package ar.edu.utn.frc.tup.lciii.dtos.common;

import lombok.Data;

@Data
public class ApuestaDto {
    private String fecha_sorteo;
    private String id_cliente;
    private String numero;
    private Integer montoApostado;
}
