package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.AllTelemetryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.DeviceDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.EndpointDevices;
import ar.edu.utn.frc.tup.lciii.dtos.common.TelemetryDTO;
import ar.edu.utn.frc.tup.lciii.repositories.DeviceRepository;
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
public class DeviceController {

    @Autowired
    private final FinalService finalService;

    @PostMapping("/device")
    ResponseEntity<DeviceDTO> postDevice(@RequestBody DeviceDTO device) {
        DeviceDTO deviceDTO = finalService.createDevice(device);
        if(deviceDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(deviceDTO); // TODO CAMBIAR POR CREATED
    }

    @GetMapping("/device")
    ResponseEntity<List<DeviceDTO>> getDevicesByType(@RequestParam(required = true) String type) {
        List<DeviceDTO> deviceDTOS = finalService.getDevicesByType(type);
        if(deviceDTOS == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(deviceDTOS);
    }

    @PostMapping("/save-consumed-devices")
    public ResponseEntity<List<EndpointDevices>> saveConsumedDevices() {
        List<EndpointDevices> devices = finalService.endpointInsert();
        if(devices == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(devices);
    }


    @GetMapping("/device/threshold")
    ResponseEntity<List<DeviceDTO>> getDevicesByThreshold(@RequestParam(required = true) Double lowThreshold,
                                                          @RequestParam(required = true) Double upThreshold) {
        List<DeviceDTO> deviceDTOS = finalService.getDevicesBetweenByConsumo(lowThreshold, upThreshold);
        if(deviceDTOS == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(deviceDTOS);
    }

}