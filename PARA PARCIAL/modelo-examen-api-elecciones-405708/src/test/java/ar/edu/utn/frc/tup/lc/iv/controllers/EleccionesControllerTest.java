package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.FileLoader;
import ar.edu.utn.frc.tup.lc.iv.dtos.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EleccionesControllerTest {

//    private static final String MOCK_DISTRITOS = "mocks/distritos.json";
//    private static final String MOCK_SECCIONES = "mocks/secciones.json";
//    private static final String MOCK_CARGOS = "mocks/cargos.json";
//    private static final String MOCK_RESULTADOS_CORDOBA = "mocks/resultado_cordoba.json";
//    private static final String MOCK_RESULTADOS_NACIONALES = "mocks/nacionales.json";
//
//    @Autowired
//    private ObjectMapper testMapper;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    void getDistritos() throws Exception {
//        List<DistritoDTO> distritos = Arrays.asList(testMapper.readValue(FileLoader.loadFrom(MOCK_DISTRITOS), DistritoDTO[].class));
//        MvcResult result = this.mockMvc.perform(get("/elecciones/distritos")).andDo(print()).andExpect(status().isOk())
//                .andReturn();
//        List<DistritoDTO> distritosResult = List.of(testMapper.readValue(result.getResponse().getContentAsString(Charset.defaultCharset()), DistritoDTO[].class));
//        assertEquals(distritos, distritosResult);
//    }
//
//    @Test
//    void getDistritoById() throws Exception {
//        List<DistritoDTO> distritos = Arrays.asList(testMapper.readValue(FileLoader.loadFrom(MOCK_DISTRITOS), DistritoDTO[].class));
//        MvcResult result = this.mockMvc.perform(get("/elecciones/distritos/4")).andDo(print()).andExpect(status().isOk())
//                .andReturn();
//        DistritoDTO distritoResult = testMapper.readValue(result.getResponse().getContentAsString(Charset.defaultCharset()), DistritoDTO.class);
//        assertEquals(distritos.get(3), distritoResult);
//    }
//
//    @Test
//    void getSeccionesByDistrito() throws Exception {
//        List<SeccionDTO> expected = Arrays.asList(testMapper.readValue(FileLoader.loadFrom(MOCK_SECCIONES), SeccionDTO[].class));
//        MvcResult result = this.mockMvc.perform(get("/elecciones/distritos/4/secciones")).andDo(print()).andExpect(status().isOk())
//                .andReturn();
//        List<SeccionDTO> actual = List.of(testMapper.readValue(result.getResponse().getContentAsString(Charset.defaultCharset()), SeccionDTO[].class));
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void getSeccionByIdAndDistritoId() throws Exception {
//        List<SeccionDTO> expected = Arrays.asList(testMapper.readValue(FileLoader.loadFrom(MOCK_SECCIONES), SeccionDTO[].class));
//        MvcResult result = this.mockMvc.perform(get("/elecciones/distritos/4/secciones/1")).andDo(print()).andExpect(status().isOk())
//                .andReturn();
//        SeccionDTO actual = testMapper.readValue(result.getResponse().getContentAsString(Charset.defaultCharset()), SeccionDTO.class);
//        assertEquals(expected.get(0), actual);
//    }
//
//    @Test
//    void getCargosDistrito() throws Exception {
//        List<CargoDTO> expected = Arrays.asList(testMapper.readValue(FileLoader.loadFrom(MOCK_CARGOS), CargoDTO[].class));
//        MvcResult result = this.mockMvc.perform(get("/elecciones/distritos/4/cargos")).andDo(print()).andExpect(status().isOk())
//                .andReturn();
//        List<CargoDTO> actual = List.of(testMapper.readValue(result.getResponse().getContentAsString(Charset.defaultCharset()), CargoDTO[].class));
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void getCargoByIdAndDistritoId() throws Exception {
//        List<CargoDTO> expected = Arrays.asList(testMapper.readValue(FileLoader.loadFrom(MOCK_CARGOS), CargoDTO[].class));
//        MvcResult result = this.mockMvc.perform(get("/elecciones/distritos/4/cargos/1")).andDo(print()).andExpect(status().isOk())
//                .andReturn();
//        CargoDTO actual = testMapper.readValue(result.getResponse().getContentAsString(Charset.defaultCharset()), CargoDTO.class);
//        assertEquals(expected.get(0), actual);
//    }
//
//    @Test
//    void getResultadosByDistrito() throws Exception {
//        ResultadoDistritoDTO expected = testMapper.readValue(FileLoader.loadFrom(MOCK_RESULTADOS_CORDOBA), ResultadoDistritoDTO.class);
//        MvcResult result = this.mockMvc.perform(get("/elecciones/distritos/4/resultados")).andDo(print()).andExpect(status().isOk())
//                .andReturn();
//        ResultadoDistritoDTO actual = testMapper.readValue(result.getResponse().getContentAsString(Charset.defaultCharset()), ResultadoDistritoDTO.class);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void getResultadosGenerales() throws Exception {
//        ResultadosDTO expected = testMapper.readValue(FileLoader.loadFrom(MOCK_RESULTADOS_NACIONALES), ResultadosDTO.class);
//        MvcResult result = this.mockMvc.perform(get("/elecciones/resultados")).andDo(print()).andExpect(status().isOk())
//                .andReturn();
//        ResultadosDTO actual = testMapper.readValue(result.getResponse().getContentAsString(Charset.defaultCharset()), ResultadosDTO.class);
//        assertEquals(expected, actual);
//    }
}