package ar.edu.utn.frc.tup.lc.iii.services;

import ar.edu.utn.frc.tup.lc.iii.models.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface PhaseService {

    Phase getPhase(Long id);
    List<Phase> getAllPhases();
    List<Phase> getAllPhases(Long raceId);
    Phase getPhaseRace(Long raceId, PhaseStage phaseStage);
    Phase getPhaseRace(Race race, PhaseStage phaseStage);
    Phase createOrUpdatePhase(Long id, Race race, PhaseStage phaseStage, LocalDateTime startDateTime,
                              LocalDateTime endDateTime, PhaseStatus phaseStatus);
    Phase startPhase(Long phaseId);
    Phase stopPhase(Long phaseId);
}
