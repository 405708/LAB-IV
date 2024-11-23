package ar.edu.utn.frc.tup.lc.iii.entities;

import ar.edu.utn.frc.tup.lc.iii.models.Team;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DriverEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastname;
    private Integer number;
    @Enumerated(EnumType.STRING)
    private Team team;

}
