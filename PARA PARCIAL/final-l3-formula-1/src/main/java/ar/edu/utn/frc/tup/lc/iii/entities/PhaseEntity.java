package ar.edu.utn.frc.tup.lc.iii.entities;

import ar.edu.utn.frc.tup.lc.iii.models.PhaseStatus;
import ar.edu.utn.frc.tup.lc.iii.models.PhaseStage;
import ar.edu.utn.frc.tup.lc.iii.models.StartingGrid;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "phases")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "race_id")
    private RaceEntity race;
    @Enumerated(EnumType.STRING)
    private PhaseStage phaseStage;

    @OneToMany(mappedBy="phase")
    private List<StartingGridEntity> startingGrid;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    @Enumerated(EnumType.STRING)
    private PhaseStatus phaseStatus;
    @OneToMany(mappedBy="phase")
    private List<PhaseResultEntity> phaseResults;
}
