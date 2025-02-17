package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.DeviceDTO;
import ar.edu.utn.frc.tup.lciii.model.Device;
import ar.edu.utn.frc.tup.lciii.model.DeviceType;
import ar.edu.utn.frc.tup.lciii.repositories.DeviceRepository;
import ar.edu.utn.frc.tup.lciii.services.FinalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeviceController.class)
@AutoConfigureWebMvc
class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FinalService finalService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL = "/api/device";

    DeviceDTO deviceDTO;
    Device device;
    List<DeviceDTO> devices;

    @BeforeEach
    void setUp() {
        deviceDTO = new DeviceDTO();
        deviceDTO.setOs("Windows");
        deviceDTO.setType(DeviceType.LAPTOP);
        deviceDTO.setMacAddress("11221");
        deviceDTO.setHostName("localhost");

        device = new Device();
        device.setOs("Windows");
        device.setMacAddress("11221");
        device.setHostName("localhost");
        device.setType(DeviceType.LAPTOP);
        device.setCreatedDate(LocalDateTime.now());

        devices = new ArrayList<>();
        devices.add(deviceDTO);

    }


    @Test
    void postDevice() throws Exception {
        when(finalService.createDevice(any(DeviceDTO.class))).thenReturn(deviceDTO);

        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(deviceDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hostName").value("localhost"));

        verify(finalService,times(1)).createDevice(any(DeviceDTO.class));
    }

    @Test
    void getDevicesByType() throws Exception {
        // Arrange: mocking the service to return a list of devices
        String type = "LAPTOP";
        when(finalService.getDevicesByType(type)).thenReturn(devices);

        mockMvc.perform(get(URL)
                        .param("type", type))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].os").value("Windows"))
                .andExpect(jsonPath("$[0].type").value("LAPTOP"))
                .andExpect(jsonPath("$[0].macAddress").value("11221"))
                .andExpect(jsonPath("$[0].hostName").value("localhost"));
    }

    @Test
    void shouldReturnBadRequestWhenNoDevicesFound() throws Exception {
        String type = "TABLET";
        when(finalService.getDevicesByType(type)).thenReturn(null);

        mockMvc.perform(get(URL)
                        .param("type", type))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByCpuUsage() throws Exception {
        Double min = 1.00;
        Double max = 2.00;
        when(finalService.getDevicesBetweenByConsumo(min, max)).thenReturn(devices);
        mockMvc.perform(get(URL + "/threshold")
                        .param("lowThreshold", min.toString())
                        .param("upThreshold", max.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].os").value("Windows"))
                .andExpect(jsonPath("$[0].type").value("LAPTOP"))
                .andExpect(jsonPath("$[0].macAddress").value("11221"))
                .andExpect(jsonPath("$[0].hostName").value("localhost"));

    }

    @Test
    void saveDevicesForRestTemplate() throws Exception {

    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}