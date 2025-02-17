package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.dtos.common.AllTelemetryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.TelemetryDTO;
import ar.edu.utn.frc.tup.lciii.model.Telemetry;
import ar.edu.utn.frc.tup.lciii.services.FinalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TelemetryController {

    @Autowired
    private final FinalService finalService;

    @PostMapping("/telemetry")
    ResponseEntity<TelemetryDTO> postTelemetry(@RequestBody TelemetryDTO telemetry) {
        TelemetryDTO telemetryDTO = finalService.createTelemetry(telemetry);
        if(telemetryDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(telemetryDTO); //TODO CAMBIAR POR CREATED
    }

    @GetMapping("/telemetry")
    ResponseEntity<List<AllTelemetryDTO>> getTelemetry(@RequestParam(required = false) String hostname) {
        List<AllTelemetryDTO> allTelemetryDTOS = finalService.getTelemetries(hostname);
        if(allTelemetryDTOS == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(allTelemetryDTOS);
    }




}