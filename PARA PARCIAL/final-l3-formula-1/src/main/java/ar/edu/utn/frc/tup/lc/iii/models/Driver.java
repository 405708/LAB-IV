package ar.edu.utn.frc.tup.lc.iii.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Driver {

    private Long id;
    private String name;
    private String lastname;
    private Integer number;
    private Team team;
}
