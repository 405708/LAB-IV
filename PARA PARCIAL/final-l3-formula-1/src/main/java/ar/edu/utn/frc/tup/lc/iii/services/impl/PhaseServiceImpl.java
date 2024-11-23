package ar.edu.utn.frc.tup.lc.iii.services.impl;

import ar.edu.utn.frc.tup.lc.iii.entities.PhaseEntity;
import ar.edu.utn.frc.tup.lc.iii.entities.RaceEntity;
import ar.edu.utn.frc.tup.lc.iii.models.*;
import ar.edu.utn.frc.tup.lc.iii.repositories.PhaseRepository;
import ar.edu.utn.frc.tup.lc.iii.services.DriverService;
import ar.edu.utn.frc.tup.lc.iii.services.PhaseService;
import ar.edu.utn.frc.tup.lc.iii.services.StartingGridService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PhaseServiceImpl implements PhaseService {

    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private StartingGridService startingGridService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Phase getPhase(Long id) {
        PhaseEntity phaseEntity = phaseRepository.findById(id).orElse(null);
        return phaseEntity != null ? modelMapper.map(phaseEntity, Phase.class) : null;
    }

    @Override
    public List<Phase> getAllPhases() {
        return modelMapper.map(phaseRepository.findAll(), new TypeToken<List<Phase>>() {}.getType());
    }

    @Override
    public List<Phase> getAllPhases(Long raceId) {
        return modelMapper.map(phaseRepository.findAllByRaceId(raceId), new org.modelmapper.TypeToken<List<Phase>>() {}.getType());
    }

    @Override
    public Phase getPhaseRace(Long raceId, PhaseStage phaseStage) {
        PhaseEntity phaseEntity = phaseRepository.findByRaceIdAndPhaseStage(raceId, phaseStage).orElse(null);
        return phaseEntity != null ? modelMapper.map(phaseEntity, Phase.class) : null;
    }

    @Override
    public Phase getPhaseRace(Race race, PhaseStage phaseStage) {
        PhaseEntity phaseEntity = phaseRepository.findByRaceIdAndPhaseStage(race.getId(), phaseStage).orElse(null);
        return phaseEntity != null ? modelMapper.map(phaseEntity, Phase.class) : null;
    }

    @Override
    public Phase createOrUpdatePhase(Long id, Race race, PhaseStage phaseStage, LocalDateTime startDateTime,
                                     LocalDateTime endDateTime, PhaseStatus phaseStatus) {
        RaceEntity raceEntity = modelMapper.map(race, RaceEntity.class);
        PhaseEntity phaseEntity = new PhaseEntity();
        phaseEntity.setId(id);
        phaseEntity.setRace(raceEntity);
        phaseEntity.setPhaseStage(phaseStage);
        phaseEntity.setStartDateTime(startDateTime);
        phaseEntity.setEndDateTime(endDateTime);
        phaseEntity.setPhaseStatus(phaseStatus);
        return modelMapper.map(phaseRepository.save(phaseEntity), Phase.class);
    }

    @Override
    public Phase startPhase(Long phaseId) {
        Phase phase = changePhaseStatus(phaseId, PhaseStatus.IN_PROGRESS);
        startingGridService.createStartingGrid(phase);
        return phase;
    }

    @Override
    public Phase stopPhase(Long phaseId) {
        Phase phase = changePhaseStatus(phaseId, PhaseStatus.FINISHED);
        if(phase.getPhaseStage() == PhaseStage.Q1 ||
                phase.getPhaseStage() == PhaseStage.Q2 ||
                phase.getPhaseStage() == PhaseStage.Q3) {
            startingGridService.calculateNextStartingGrid(phase);
        }
        return phase;
    }

    private Phase changePhaseStatus(Long phaseId, PhaseStatus phaseStatus) {
        PhaseEntity phaseEntity = phaseRepository.findById(phaseId).orElseThrow(
                () -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Phase does not exist")
        );
        if (phaseEntity.getPhaseStatus() != PhaseStatus.PENDING && phaseStatus == PhaseStatus.IN_PROGRESS
            || phaseEntity.getPhaseStatus() != PhaseStatus.IN_PROGRESS && phaseStatus == PhaseStatus.FINISHED) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Can't change the phase status from "
                    + phaseEntity.getPhaseStatus() + " to "
                    + phaseStatus + " status.");
        }
        phaseEntity.setPhaseStatus(phaseStatus);
        phaseRepository.save(phaseEntity);
        return modelMapper.map(phaseEntity, Phase.class);
    }
}
