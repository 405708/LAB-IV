package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.common.AllTelemetryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.DeviceDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.EndpointDevices;
import ar.edu.utn.frc.tup.lciii.dtos.common.TelemetryDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FinalService {
    TelemetryDTO createTelemetry(TelemetryDTO telemetry);
    DeviceDTO createDevice(DeviceDTO device);
    List<AllTelemetryDTO> getTelemetries(String hostname);
    List<DeviceDTO> getDevicesByType(String type);
    List<DeviceDTO> getDevicesBetweenByConsumo(Double min, Double max);
    List<EndpointDevices> endpointInsert();
}
