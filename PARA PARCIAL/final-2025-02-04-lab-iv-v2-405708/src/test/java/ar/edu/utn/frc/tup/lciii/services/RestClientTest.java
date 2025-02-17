package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.common.EndpointDevices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class RestClientTest {

    @MockBean
    private RestTemplate restTemplate;

    @InjectMocks
    private RestClient restClient;

    @Test
    void getDevices() {
        // Preparar datos mock
        List<Map<String, Object>> mockResponse = Arrays.asList(
                createDevicesMap("Device-01",
                        "2025-02-16T18:40:28.117-03:00",
                        "Windows 11", "00:1A:2B:3C:4D:5E",
                        "Sensor",
                        "1"),
                createDevicesMap("Device-02", "2025-02-16T18:45:10.217-03:00", "Linux Ubuntu 22.04", "AA:BB:CC:DD:EE:FF", "Router", "2")
        );

        //Configurar el mock
        when(restTemplate.getForObject(
                eq("https://67b25dc6bc0165def8cd5fdf.mockapi.io/device/device"),
                eq(List.class)
        )).thenReturn(mockResponse);

        List<EndpointDevices> devices = restClient.getDevices();

        verify(restTemplate).getForObject(
                eq("https://67b25dc6bc0165def8cd5fdf.mockapi.io/device/device"),
                eq(List.class)
        );

        assertNotNull(devices);
        assertEquals(2, devices.size());
        assertEquals("Device-01", devices.get(0).getHostName());
        assertEquals("2025-02-16T18:40:28.117-03:00",devices.get(0).getCreatedDate());
        assertEquals("Windows 11",devices.get(0).getOs());
        assertEquals("Sensor",devices.get(0).getType());
        assertEquals("1",devices.get(0).getId());
        assertEquals("00:1A:2B:3C:4D:5E", devices.get(0).getMacAddress());

    }

    // Método helper para crear el Map de respuesta mock
    private Map<String, Object> createDevicesMap(String hostName,
                                                 String createdDate,
                                                 String os,
                                                 String macAddress,
                                                 String type,
                                                 String id
    )
    {
        Map<String, Object> devices = new HashMap<>();
        devices.put("hostName", hostName);
        devices.put("createdDate", createdDate);
        devices.put("os", os);
        devices.put("macAddress", macAddress);
        devices.put("type", type);
        devices.put("id", id);

        return devices;
    }
}