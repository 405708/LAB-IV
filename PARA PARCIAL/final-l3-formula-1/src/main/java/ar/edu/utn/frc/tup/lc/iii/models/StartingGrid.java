package ar.edu.utn.frc.tup.lc.iii.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StartingGrid {

    private Long id;
    private Integer position;
    private Driver driver;
    private Phase phase;
}
