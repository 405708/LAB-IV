package ar.edu.utn.frc.tup.lc.iii.controllers;

import ar.edu.utn.frc.tup.lc.iii.dtos.LapRecordRequestDto;
import ar.edu.utn.frc.tup.lc.iii.dtos.PhaseDto;
import ar.edu.utn.frc.tup.lc.iii.dtos.PhaseResultDto;
import ar.edu.utn.frc.tup.lc.iii.dtos.RaceDto;
import ar.edu.utn.frc.tup.lc.iii.models.Phase;
import ar.edu.utn.frc.tup.lc.iii.models.PhaseStage;
import ar.edu.utn.frc.tup.lc.iii.models.PhaseStatus;
import ar.edu.utn.frc.tup.lc.iii.models.Race;
import ar.edu.utn.frc.tup.lc.iii.services.RaceService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/races")
public class RaceController {

    @Autowired
    private RaceService raceService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity<List<RaceDto>> getRaces(){
        return ResponseEntity.ok(modelMapper.map(raceService.getAllRaces(), new TypeToken<List<RaceDto>>() {}.getType()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RaceDto> getRace(@PathVariable("id") Long id) {
        return ResponseEntity.ok(modelMapper.map(raceService.getRaceById(id), RaceDto.class));
    }

    @PutMapping("")
    public ResponseEntity<RaceDto> createOrUpdateRace(@RequestBody RaceDto raceDto) {
        return ResponseEntity.ok(
                modelMapper.map(
                        raceService.createOrUpdateRace(
                                raceDto.getId(),
                                raceDto.getName(),
                                raceDto.getCircuit(),
                                raceDto.getRaceDate()),
                        RaceDto.class));
    }

    @GetMapping("/{raceId}/phases")
    public ResponseEntity<List<PhaseDto>> getRacePhases(@PathVariable("raceId") Long raceId) {
        return ResponseEntity.ok(modelMapper.map(raceService.getRacePhases(raceId), new TypeToken<List<PhaseDto>>() {}.getType()));
    }

    @GetMapping("/{raceId}/phases/{phaseStage}")
    public ResponseEntity<PhaseDto> getRacePhaseByRaceAndStage(@PathVariable("raceId") Long raceId,
                                                  @PathVariable("phaseStage") PhaseStage phaseStage) {
        Phase phase = raceService.getRacePhase(raceId, phaseStage);
        return ResponseEntity.ok(modelMapper.map(phase, PhaseDto.class));
    }

    @PutMapping("/{raceId}/phases/{phaseStage}/start")
    public ResponseEntity<PhaseDto> startRacePhase(@PathVariable("raceId") Long raceId,
                                                   @PathVariable("phaseStage") PhaseStage phaseStage) {
        return ResponseEntity.ok(modelMapper.map(raceService.startRacePhase(raceId, phaseStage), PhaseDto.class));
    }

    @PutMapping("/{raceId}/phases/{phaseStage}/stop")
    public ResponseEntity<PhaseDto> stopRacePhase(@PathVariable("raceId") Long raceId,
                                                  @PathVariable("phaseStage") PhaseStage phaseStage) {
        return ResponseEntity.ok(modelMapper.map(raceService.stopRacePhase(raceId, phaseStage), PhaseDto.class));
    }

    @PostMapping("/{raceId}/phases/{phaseStage}/laps")
    public ResponseEntity<PhaseResultDto> recordLap(@PathVariable("raceId") Long raceId,
                                                    @PathVariable("phaseStage") PhaseStage phaseStage,
                                                    @RequestBody LapRecordRequestDto lap) {
        return ResponseEntity.ok(modelMapper.map(raceService.recordLap(raceId, phaseStage, lap.getDriverId(), lap.getTime()), PhaseResultDto.class));
    }
}
