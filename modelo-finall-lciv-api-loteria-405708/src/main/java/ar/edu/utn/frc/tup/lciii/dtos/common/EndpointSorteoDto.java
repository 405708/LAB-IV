package ar.edu.utn.frc.tup.lciii.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndpointSorteoDto {
    private Integer numeroSorteo;
    private String fecha;
    private Integer dineroTotalAcumulado;
    private List<List<Integer>> numerosSorteados = new ArrayList<>();
}
