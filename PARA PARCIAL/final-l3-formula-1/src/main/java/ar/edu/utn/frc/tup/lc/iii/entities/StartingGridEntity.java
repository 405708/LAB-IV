package ar.edu.utn.frc.tup.lc.iii.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;

@Entity
@Table(name = "starting_grids")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StartingGridEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer position;
    @ManyToOne
    @JoinColumn(name = "phase_id")
    private PhaseEntity phase;
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private DriverEntity driver;

}
