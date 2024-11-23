package ar.edu.utn.frc.tup.lc.iii.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;

@Entity
@Table(name = "phase_result_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhaseResultDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "phase_result_id")
    private PhaseResultEntity phaseResult;
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private DriverEntity driver;
    private Integer lap;
    private Duration time;
}
