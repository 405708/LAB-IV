package ar.edu.utn.frc.tup.lc.iii.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Phase {

    private Long id;
    private Race race;
    private PhaseStage phaseStage;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private List<StartingGrid> startingGrid;
    private PhaseStatus phaseStatus;
    private List<PhaseResult> phaseResults;
}
