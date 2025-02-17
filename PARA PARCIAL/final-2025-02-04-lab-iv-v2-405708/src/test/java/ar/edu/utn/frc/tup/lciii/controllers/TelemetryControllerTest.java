package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.AllTelemetryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.TelemetryDTO;
import ar.edu.utn.frc.tup.lciii.services.FinalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(TelemetryController.class)
class TelemetryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FinalService finalService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL = "/api/telemetry";

    TelemetryDTO telemetry;
    AllTelemetryDTO allTelemetryDTO;
    AllTelemetryDTO allTelemetryDTO2;
    List<AllTelemetryDTO> allTelemetryDTOList;

    @BeforeEach
    void setUp() {
        telemetry = new TelemetryDTO();
        telemetry.setHostname("localhost");
        telemetry.setIp("127.0.0.1");
        telemetry.setCpuUsage(10.10);
        telemetry.setScreenCaptureAllowed(true);
        telemetry.setAudioCaptureAllowed(true);

        allTelemetryDTO = new AllTelemetryDTO();
        allTelemetryDTO.setHostname("localhost");
        allTelemetryDTO.setIp("127.0.0.1");
        allTelemetryDTO.setScreenCaptureAllowed(true);
        allTelemetryDTO.setAudioCaptureAllowed(true);

        allTelemetryDTO2 = new AllTelemetryDTO();
        allTelemetryDTO2.setHostname("hostext");
        allTelemetryDTO2.setIp("125.0.0.1");
        allTelemetryDTO2.setScreenCaptureAllowed(true);
        allTelemetryDTO2.setAudioCaptureAllowed(true);

        allTelemetryDTOList = new ArrayList<>();
        allTelemetryDTOList.add(allTelemetryDTO);
        allTelemetryDTOList.add(allTelemetryDTO2);

    }

    @Test
    void postTelemetry() throws Exception {
        when(finalService.createTelemetry(any(TelemetryDTO.class))).thenReturn(telemetry);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(telemetry)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hostname").value("localhost"))
                .andExpect(jsonPath("$.ip").value("127.0.0.1"))
                .andExpect(jsonPath("$.cpuUsage").value(10.10));

        verify(finalService, times(1)).createTelemetry(any(TelemetryDTO.class));

    }

    @Test
    void getTelemetry() throws Exception {
        when(finalService.getTelemetries(any())).thenReturn(allTelemetryDTOList);
        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].hostname").value("localhost"))
                .andExpect(jsonPath("$[0].ip").value("127.0.0.1"));
    }


    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}