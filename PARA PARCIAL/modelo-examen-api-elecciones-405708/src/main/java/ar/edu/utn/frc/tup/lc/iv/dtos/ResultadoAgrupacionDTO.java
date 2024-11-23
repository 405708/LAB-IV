package ar.edu.utn.frc.tup.lc.iv.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultadoAgrupacionDTO {

    /**
     * Nombre de la agrupación o tipo de voto.
     * En el caso que sean votos positivos, el nombre de la agrupación.
     * En caso que no sean positivos, el tipo de voto.
     */
    private String nombre;

    /**
     * Posición de la agrupación en el distrito o a nivel nacional
     * dependiendo del contexto donde se utilice el DTO.
     * Por ejemplo, en el contexto de ResultadoDistritoDTO, la posición es sobre el distrito.
     * Si se utiliza en el contexto de ResultadosDTO, la posición es sobre el padrón nacional.
     */
    private Integer posicion;

    /**
     * Cantidad de votos obtenidos por la agrupación en el distrito o a nivel nacional
     * dependiendo del contexto donde se utilice el DTO.
     * Por ejemplo, en el contexto de ResultadoDistritoDTO, la cantidad de votos es sobre el distrito.
     * Si se utiliza en el contexto de ResultadosDTO, la cantidad de votos es sobre el padrón nacional.
     */
    private Integer votos;

    /**
     * Porcentaje de votos obtenidos por la agrupación en el distrito o a nivel nacional
     * dependiendo del contexto donde se utilice el DTO.
     * Por ejemplo, en el contexto de ResultadoDistritoDTO, el porcentaje de votos es sobre el distrito.
     * Si se utiliza en el contexto de ResultadosDTO, el porcentaje de votos es sobre el padrón nacional.
     */
    private String porcentaje;
}
