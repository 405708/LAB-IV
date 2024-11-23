package ar.edu.utn.frc.tup.lc.iv.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultadosDTO {

    /**
     * Cantidad de votos escrutados.
     * Igual a la suma de todas las votos a nivel nacional.
     */
    @JsonProperty("votos_escrutados")
    private Integer votosEscrutados;

    /**
     * Lista de nombres de las distritos que componen el padrón nacional
     */
    private List<String> distritos;

    /**
     * Nombre de la agrupación ganadora en el distrito
     */
    @JsonProperty("agrupacion_ganadora")
    private String agrupacionGanadora;

    /**
     * Lista de resultados de las agrupaciones a nivel nacional
     */
    @JsonProperty("resultados_nacionales")
    private List<ResultadoAgrupacionDTO> resultadosNacionales;

    /**
     * Lista de resultados de las agrupaciones en cada distrito
     */
    @JsonProperty("resultados_distritos")
    private List<ResultadoDistritoDTO> resultadoDistritos;
}
