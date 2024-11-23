package ar.edu.utn.frc.tup.lc.iii.services.impl;

import ar.edu.utn.frc.tup.lc.iii.entities.RaceEntity;
import ar.edu.utn.frc.tup.lc.iii.models.*;
import ar.edu.utn.frc.tup.lc.iii.repositories.RaceRepository;
import ar.edu.utn.frc.tup.lc.iii.services.PhaseResultService;
import ar.edu.utn.frc.tup.lc.iii.services.PhaseService;
import ar.edu.utn.frc.tup.lc.iii.services.RaceService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class RaceServiceImpl implements RaceService {

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private PhaseService phaseService;

    @Autowired
    private PhaseResultService phaseResultService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Race> getAllRaces() {
        return modelMapper.map(raceRepository.findAll(), new TypeToken<List<Race>>() {}.getType());
    }

    @Override
    public Race getRaceById(Long id) {
        return modelMapper.map(raceRepository.findById(id).orElse(null), Race.class);
    }

    @Override
    public Race createOrUpdateRace(Long id, String name, String circuit, LocalDate raceDate) {
        RaceEntity raceEntity = new RaceEntity(id, name, raceDate, circuit);
        return modelMapper.map(raceRepository.save(raceEntity), Race.class);
    }

    @Override
    public List<Phase> getRacePhases(Long raceId) {
        return phaseService.getAllPhases(raceId);
    }

    @Override
    public Phase getRacePhase(Long raceId, PhaseStage phaseStage) {
        return phaseService.getPhaseRace(raceId, phaseStage);
    }

    @Override
    public Phase createOrUpdatePhaseRace(Long phaseId, Long raceId, PhaseStage phaseStage, LocalDateTime startDateTime,
                                         LocalDateTime endDateTime, PhaseStatus phaseStatus) {
        Race race = modelMapper.map(raceRepository.findById(raceId).orElse(null), Race.class);
        if(race == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Race does not exist");
        }
        return phaseService.createOrUpdatePhase(phaseId, race, phaseStage, startDateTime, endDateTime, phaseStatus);
    }

    @Override
    public Phase startRacePhase(Long raceId, PhaseStage phaseStage) {
        Phase phase = phaseService.getPhaseRace(raceId, phaseStage);
        if(phase == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Can't find the phase race");
        }
        return phaseService.startPhase(phase.getId());
    }

    @Override
    public Phase stopRacePhase(Long raceId, PhaseStage phaseStage) {
        Phase phase = phaseService.getPhaseRace(raceId, phaseStage);
        if(phase == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Can't find the phase race");
        }
        return phaseService.stopPhase(phase.getId());
    }

    @Override
    public PhaseResult recordLap(Long raceId, PhaseStage phaseStage, Long driverId, Duration time) {
        Phase phase = phaseService.getPhaseRace(raceId, phaseStage);
        if(phase == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Can't find the phase race");
        }
        if(phase.getPhaseStatus() != PhaseStatus.IN_PROGRESS) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Phase is not in progress");
        }
        return phaseResultService.recordLap(phase, driverId, time);
    }
}
