package ar.edu.utn.frc.tup.lc.iii.services;

import ar.edu.utn.frc.tup.lc.iii.models.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface RaceService {

    List<Race> getAllRaces();
    Race getRaceById(Long id);
    Race createOrUpdateRace(Long id, String name, String circuit, LocalDate raceDate);
    List<Phase> getRacePhases(Long raceId);
    Phase getRacePhase(Long raceId, PhaseStage phaseStage);
    Phase createOrUpdatePhaseRace(Long phaseId, Long raceId, PhaseStage phaseStage, LocalDateTime startDateTime,
                                  LocalDateTime endDateTime, PhaseStatus phaseStatus);
    Phase startRacePhase(Long raceId, PhaseStage phaseStage);
    Phase stopRacePhase(Long raceId, PhaseStage phaseStage);
    PhaseResult recordLap(Long raceId, PhaseStage phaseStage, Long driverId, Duration time);

}
