package ar.edu.utn.frc.tup.lc.iii.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.util.List;

@Entity
@Table(name = "phase_results")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhaseResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "phase_id")
    private PhaseEntity phase;
    private Integer position;
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private DriverEntity driver;
    private Duration time;
    private Integer laps;
    @OneToMany(mappedBy = "phaseResult")
    private List<PhaseResultDetailEntity> details;

}
