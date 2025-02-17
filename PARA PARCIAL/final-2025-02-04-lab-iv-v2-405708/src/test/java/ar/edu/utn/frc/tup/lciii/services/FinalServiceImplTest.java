package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.common.AllTelemetryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.DeviceDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.TelemetryDTO;
import ar.edu.utn.frc.tup.lciii.model.Device;
import ar.edu.utn.frc.tup.lciii.model.DeviceType;
import ar.edu.utn.frc.tup.lciii.model.Telemetry;
import ar.edu.utn.frc.tup.lciii.repositories.DeviceRepository;
import ar.edu.utn.frc.tup.lciii.repositories.TelemetryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class FinalServiceImplTest {

    @InjectMocks
    private FinalServiceImpl finalService;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private TelemetryRepository telemetryRepository;

    DeviceDTO deviceDTO1;
    Device device;
    Telemetry telemetry;
    TelemetryDTO telemetry2;
    AllTelemetryDTO allTelemetryDTO;

    List<Telemetry> telemetryList;
    List<TelemetryDTO> telemetryDTOList;
    List<AllTelemetryDTO> allTelemetryDTOList;
    List<Device> deviceList;

    @BeforeEach
    void setUp() {
        deviceDTO1 = new DeviceDTO();
        deviceDTO1.setOs("Windows");
        deviceDTO1.setType(DeviceType.LAPTOP);
        deviceDTO1.setMacAddress("11221");
        deviceDTO1.setHostName("localhost");

        device = new Device();
        device.setOs("Windows");
        device.setMacAddress("11221");
        device.setHostName("localhost");
        device.setType(DeviceType.LAPTOP);
        device.setCreatedDate(LocalDateTime.now());

        telemetry = new Telemetry();
        telemetry.setDevice(device);
        telemetry.setHostname(device.getHostName());
        telemetry.setIp("127.0.0.1");
        telemetry.setCpuUsage(10.1);
        telemetry.setMicrophoneState("state");
        telemetry.setScreenCaptureAllowed(true);
        telemetry.setAudioCaptureAllowed(true);
        telemetry2 = new TelemetryDTO();
        telemetry2.setHostname(device.getHostName());
        telemetry2.setCpuUsage(10.1);
        telemetry2.setIp("127.0.0.1");
        telemetry2.setMicrophoneState("state");
        telemetry2.setScreenCaptureAllowed(true);
        telemetry2.setAudioCaptureAllowed(true);


        allTelemetryDTO = new AllTelemetryDTO();
        allTelemetryDTO.setHostname(device.getHostName());
        telemetryDTOList = new ArrayList<>();
        allTelemetryDTOList = new ArrayList<>();
        telemetryDTOList.add(telemetry2);
        allTelemetryDTOList.add(allTelemetryDTO);
        telemetryList = new ArrayList<>();
        telemetryList.add(telemetry);
        deviceList = new ArrayList<>();
        deviceList.add(device);


    }


    @Test
    void createTelemetry() {
        when(deviceRepository.getDeviceByHostName("localhost")).thenReturn(device);
        when(telemetryRepository.save(any(Telemetry.class))).thenReturn(telemetry);

        TelemetryDTO telemetryDTO = finalService.createTelemetry(telemetry2);
        assertNotNull(telemetryDTO);
        assertEquals(telemetryDTO.getHostname(), telemetry.getHostname());
        assertEquals(telemetryDTO.getCpuUsage(), telemetry.getCpuUsage());
        assertEquals(telemetryDTO.getIp(), telemetry.getIp());
        assertEquals(telemetryDTO.getMicrophoneState(), telemetry.getMicrophoneState());
        assertEquals(telemetryDTO.getScreenCaptureAllowed(), telemetry.getScreenCaptureAllowed());
        assertEquals(telemetryDTO.getAudioCaptureAllowed(), telemetry.getAudioCaptureAllowed());

        verify(deviceRepository, times(1)).save(device);

        verify(telemetryRepository, times(1)).save(any(Telemetry.class));
    }

    @Test
    void createDevice() {
        when(deviceRepository.save(any(Device.class))).thenReturn(device);
        when(deviceRepository.getDeviceByHostName("localhost")).thenReturn(null);

        DeviceDTO devicee = finalService.createDevice(deviceDTO1);
        assertNotNull(devicee);
        assertEquals(deviceDTO1.getOs(), devicee.getOs());
        assertEquals(deviceDTO1.getType(), devicee.getType());
        assertEquals(deviceDTO1.getMacAddress(), devicee.getMacAddress());
        assertEquals(deviceDTO1.getHostName(), devicee.getHostName());

        verify(deviceRepository, times(1)).save(any(Device.class));

        verify(deviceRepository, times(1)).getDeviceByHostName("localhost");

    }

    @Test
    void getTelemetries() {

    when(telemetryRepository.findAll()).thenReturn(telemetryList);

    List<AllTelemetryDTO> response = finalService.getTelemetries("localhost");
    assertNotNull(response);
    assertEquals(telemetryList.size(), response.size());
    assertEquals(telemetryList.get(0).getHostname(), response.get(0).getHostname());
    assertEquals(telemetryList.get(0).getIp(), response.get(0).getIp());


    }

    @Test
    void getDevicesByType() {
        when(deviceRepository.findAll()).thenReturn(deviceList);
        List<DeviceDTO> response = finalService.getDevicesByType("LAPTOP");
        assertNotNull(response);
        assertEquals(telemetryList.size(), response.size());

    }


    @Test
    void getDevicesBetweenByConsumo() {
    }

    @Test
    void endpointInsert() {
    }
}