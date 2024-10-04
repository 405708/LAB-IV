package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.ApuestaDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.EndpointSorteoDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.SaveApuestaDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.SorteoBetsDto;
import ar.edu.utn.frc.tup.lciii.services.ApuestaService;
import ar.edu.utn.frc.tup.lciii.services.SorteoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.assertj.core.api.Assertions.assertThat;

class LoteriaControllerTest {

    private static final String URL_APUESTAS = "/loteria/apuestas";
    private static final String URL_SORTEOS = "/loteria/sorteo/{id_sorteo}";

    private MockMvc mockMvc;

    @InjectMocks
    private LoteriaController loteriaController;
    @Mock
    private ApuestaService apuestaService;
    @Mock
    private SorteoService sorteoService;

    SaveApuestaDto saveApuestaDto;
    ApuestaDto apuestaDto;
    SorteoBetsDto sorteoBetsDto;

    @BeforeEach
    void SetUp(){
        initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(loteriaController).build();
        apuestaDto = new ApuestaDto();
        apuestaDto.setFecha_sorteo("2024-01-16");
        apuestaDto.setNumero("10");
        apuestaDto.setId_cliente("Pedro");
        apuestaDto.setMontoApostado(50);

        saveApuestaDto = new SaveApuestaDto();

        saveApuestaDto.setId_sorteo(123); // Asignar un valor de ejemplo
        saveApuestaDto.setFecha_sorteo("2024-01-16");
        saveApuestaDto.setId_cliente("Pedro");
        saveApuestaDto.setNumero("10");
        saveApuestaDto.setResultado(null); // Si no tienes un resultado aún, lo puedes dejar null

        sorteoBetsDto = new SorteoBetsDto();
        sorteoBetsDto.setFecha_sorteo("2024-01-16");
        sorteoBetsDto.setTotalEnReserva(1000);
        sorteoBetsDto.setId_sorteo(123);
        sorteoBetsDto.setApuestas(new ArrayList<>());
    }
    @Test
    void endpoint_post_apuestas_success() throws Exception {

        when(apuestaService.save(any(ApuestaDto.class))).thenReturn(saveApuestaDto);

        mockMvc.perform(post(URL_APUESTAS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(apuestaDto))) // Convertir el DTO de request a JSON
                .andExpect(status().isOk()) // Verificar que el status es 200 OK
                .andExpect(jsonPath("$.id_sorteo").value(123))
                .andExpect(jsonPath("$.id_cliente").value("Pedro"))
                .andExpect(jsonPath("$.fecha_sorteo").value("2024-01-16"))
                .andExpect(jsonPath("$.numero").value("10"));

        // Verificar que el servicio fue llamado una vez con el DTO correcto
        verify(apuestaService, times(1)).save(any(ApuestaDto.class));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void endpoint_post_apuesta_bad_gateway() throws Exception{
        when(apuestaService.save(any(ApuestaDto.class))).thenReturn(null);

        mockMvc.perform(post(URL_APUESTAS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(apuestaDto))) // Convertir el DTO de request a JSON
                .andExpect(status().isBadGateway()); // Verificar que el status es 502 Bad Gateway

        // Verificar que el servicio fue llamado una vez
        verify(apuestaService, times(1)).save(any(ApuestaDto.class));
    }

    @Test
    void endpoint_sorteos_by_id_success() throws Exception {

        Integer idToFind = 123;

        when(sorteoService.getByIdWithBet(idToFind)).thenReturn(sorteoBetsDto);

        //SI ES CON PARAMETROS HAY QUE PASARSELOS ACA
        MockHttpServletResponse response = this.mockMvc.perform(get(URL_SORTEOS, idToFind)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(asJsonString(sorteoBetsDto));
    }

    @Test
    void endpoint_sorteos_by_id_not_found() throws Exception {
        // Un ID que no existe en la base de datos
        Integer idToFind = 999;

        when(sorteoService.getByIdWithBet(idToFind)).thenReturn(null); // Simulamos que el servicio no encuentra nada

        mockMvc.perform(get(URL_SORTEOS, idToFind)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Verificamos que el status es 404 NOT_FOUND

        verify(sorteoService, times(1)).getByIdWithBet(idToFind); // Verificamos que el servicio fue llamado una vez
    }


}