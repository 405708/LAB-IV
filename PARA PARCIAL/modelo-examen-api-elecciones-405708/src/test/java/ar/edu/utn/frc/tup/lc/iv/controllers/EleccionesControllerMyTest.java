package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.CargoDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.DistritoDTO;
import ar.edu.utn.frc.tup.lc.iv.dtos.SeccionDTO;
import ar.edu.utn.frc.tup.lc.iv.services.EleccionesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EleccionesController.class)
@AutoConfigureWebMvc
public class EleccionesControllerMyTest {
    private static final String URL_BASE = "/elecciones";
    private static final String URL_DISTRITO = URL_BASE + "/distritos";
    private static final String URL_RESULTADOS = "/resultados";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EleccionesService eleccionesService;
    @Autowired
    private ObjectMapper objectMapper;

    DistritoDTO distritoDTO1;
    DistritoDTO distritoDTO2;
    DistritoDTO distritoDTO3;
    List<DistritoDTO> mockDistritos;

    CargoDTO cargoDTO;
    CargoDTO cargoDTO1;
    CargoDTO cargoDTO2;
    List<CargoDTO> mockCargos;

    SeccionDTO seccionDTO;
    SeccionDTO seccionDTO1;
    SeccionDTO seccionDTO2;
    List<SeccionDTO> mockSecciones;

    @BeforeEach
    void setUp(){
        distritoDTO1 = new DistritoDTO();
        distritoDTO1.setId(1L);
        distritoDTO1.setNombre("Pampa");

        distritoDTO2 = new DistritoDTO();
        distritoDTO2.setId(2L);
        distritoDTO2.setNombre("Jujuy");

        distritoDTO3 = new DistritoDTO();
        distritoDTO3.setId(3L);
        distritoDTO3.setNombre("Yrigoyen");

        mockDistritos = new ArrayList<>();
        mockDistritos.add(distritoDTO1);
        mockDistritos.add(distritoDTO2);
        mockDistritos.add(distritoDTO3);

        cargoDTO = new CargoDTO();
        cargoDTO.setId(1L);
        cargoDTO.setNombre("Pampa");

        cargoDTO1 = new CargoDTO();
        cargoDTO1.setId(2L);
        cargoDTO1.setNombre("Jujuy");

        cargoDTO2 = new CargoDTO();
        cargoDTO2.setId(3L);
        cargoDTO2.setNombre("Yrigoyen");

        mockCargos = new ArrayList<>();
        mockCargos.add(cargoDTO);
        mockCargos.add(cargoDTO1);
        mockCargos.add(cargoDTO2);

        seccionDTO = new SeccionDTO();
        seccionDTO.setId(1L);
        seccionDTO.setNombre("Pampa");

        seccionDTO1 = new SeccionDTO();
        seccionDTO1.setId(2L);
        seccionDTO1.setNombre("Jujuy");

        seccionDTO2 = new SeccionDTO();
        seccionDTO2.setId(3L);
        seccionDTO2.setNombre("Yrigoyen");

        mockSecciones = new ArrayList<>();
        mockSecciones.add(seccionDTO);
        mockSecciones.add(seccionDTO1);
        mockSecciones.add(seccionDTO2);
    }

    @Test
    void getAllDistritos_Success() throws Exception {
        //when
        when(eleccionesService.getDistritosAllOrByName(null)).thenReturn(mockDistritos);

        //then
        mockMvc.perform(get(URL_DISTRITO)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Pampa"))
                .andExpect(jsonPath("$.size()").value(3));

        verify(eleccionesService, times(1)).getDistritosAllOrByName(null);
    }
    @Test
    void getAllDistritos_NotFound() throws Exception {
        //when
        when(eleccionesService.getDistritosAllOrByName("Pedro")).thenReturn(Collections.emptyList());

        //then
        mockMvc.perform(get(URL_DISTRITO )
                        .param("distrito_name", "Pedro")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(eleccionesService, times(1)).getDistritosAllOrByName("Pedro");
    }

    @Test
    void distritos_by_id_success() throws Exception {
        Long idToFind = 1L;

        when(eleccionesService.getDistritoById(idToFind)).thenReturn(distritoDTO1);

        MockHttpServletResponse response = this.mockMvc.perform(get(URL_DISTRITO + "/" + idToFind) // ID en la URL
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(asJsonString(distritoDTO1));
    }

    @Test
    void distritos_by_id_not_found() throws Exception {

        Long idToFind = 99L;

        when(eleccionesService.getDistritoById(idToFind)).thenReturn(null);


        MockHttpServletResponse response = this.mockMvc.perform(get(URL_DISTRITO + "/" + idToFind) // ID en la URL
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void distritos_by_id_cargos_success() throws Exception {
        Long idToFind = 1L;

        when(eleccionesService.getCargosDistrito(idToFind)).thenReturn(mockCargos);

        this.mockMvc.perform(get(URL_DISTRITO + "/" + idToFind + "/cargos") // ID en la URL
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Pampa"))
                .andExpect(jsonPath("$.size()").value(3));

        verify(eleccionesService, times(1)).getCargosDistrito(idToFind);

    }

    @Test
    void distritos_by_id_cargos_not_found() throws Exception {

        Long idToFind = 99L;

        when(eleccionesService.getCargosDistrito(idToFind)).thenReturn(Collections.emptyList());


        MockHttpServletResponse response = this.mockMvc.perform(get(URL_DISTRITO + "/" + idToFind + "/cargos") // ID en la URL
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void distritos_by_id_cargos_id_success() throws Exception {
        Long idToFind = 1L;
        Long idCargo = 3L;

        when(eleccionesService.getCargoByIdAndDistritoId(idToFind, idCargo)).thenReturn(cargoDTO2);


        MockHttpServletResponse response = this.mockMvc.perform(get(URL_DISTRITO + "/" + idToFind + "/cargos/" + idCargo) // ID en la URL
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse();


        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(asJsonString(cargoDTO2));

    }

    @Test
    void distritos_by_id_cargos_id_not_found() throws Exception {

        Long idToFind = 99L;
        Long idCargo = 49L;

        when(eleccionesService.getCargoByIdAndDistritoId(idToFind, idCargo)).thenReturn(null);


        MockHttpServletResponse response = this.mockMvc.perform(get(URL_DISTRITO + "/" + idToFind + "/cargos/" + idCargo) // ID en la URL
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void distritos_by_id_secciones_success() throws Exception {
        Long idToFind = 1L;

        when(eleccionesService.getSeccionesByDistrito(idToFind)).thenReturn(mockSecciones);

        this.mockMvc.perform(get(URL_DISTRITO + "/" + idToFind + "/secciones") // ID en la URL
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Pampa"))
                .andExpect(jsonPath("$.size()").value(3));

        verify(eleccionesService, times(1)).getSeccionesByDistrito(idToFind);

    }

    @Test
    void distritos_by_id_secciones_not_found() throws Exception {

        Long idToFind = 99L;

        when(eleccionesService.getSeccionesByDistrito(idToFind)).thenReturn(Collections.emptyList());


        MockHttpServletResponse response = this.mockMvc.perform(get(URL_DISTRITO + "/" + idToFind + "/secciones") // ID en la URL
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void distritos_by_id_secciones_id_success() throws Exception {
        Long idToFind = 1L;
        Long idSeccion = 3L;

        when(eleccionesService.getSeccionByIdAndDistritoId(idToFind, idSeccion)).thenReturn(seccionDTO2);


        MockHttpServletResponse response = this.mockMvc.perform(get(URL_DISTRITO + "/" + idToFind + "/secciones/" + idSeccion) // ID en la URL
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(asJsonString(seccionDTO2));

    }

    @Test
    void distritos_by_id_secciones_id_not_found() throws Exception {

        Long idToFind = 99L;
        Long idSeccion = 49L;

        when(eleccionesService.getCargoByIdAndDistritoId(idToFind, idSeccion)).thenReturn(null);


        MockHttpServletResponse response = this.mockMvc.perform(get(URL_DISTRITO + "/" + idToFind + "/secciones/" + idSeccion) // ID en la URL
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }







    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
