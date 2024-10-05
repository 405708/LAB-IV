package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.domains.Position;
import ar.edu.utn.frc.tup.lciii.dtos.common.*;
import ar.edu.utn.frc.tup.lciii.services.PlayerService;
import ar.edu.utn.frc.tup.lciii.services.RestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@SpringBootTest
@AutoConfigureMockMvc
class RugbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PlayerService playerService;

    @MockBean
    private RestClient restClient;

    @Autowired
    private ObjectMapper objectMapper;

    private ResponsePlayerDto mockPlayerResponse;
    private PostPlayerDto mockPost;
    private EndpointTeamsDto mockTeam;
    private TeamsPlayersDto mockTeamWithPlayers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new RugbyController(playerService))
                .build();

        // Setup existing mock data
        mockPlayerResponse = new ResponsePlayerDto();
        mockPlayerResponse.setEquipoId(1);
        mockPlayerResponse.setTeamName("Les Blues");
        mockPlayerResponse.setFullname("pEDRO");
        mockPlayerResponse.setPosition(Position.ATTACKER);
        mockPlayerResponse.setRating(50);
        mockPlayerResponse.setValue(1000);

        mockPost = new PostPlayerDto();
        mockPost.setEquipoId(1);
        mockPost.setFullname("pEDRO");
        mockPost.setPosition("DC");
        mockPost.setShot(50);
        mockPost.setSkills(50);
        mockPost.setSprint(50);
        mockPost.setValue(1000);

        mockTeam = new EndpointTeamsDto();
        mockTeam.setId(1);
        mockTeam.setName("Les Blues");

        // Setup new mock data for team with players
        mockTeamWithPlayers = new TeamsPlayersDto();
        mockTeamWithPlayers.setId(1);
        mockTeamWithPlayers.setName("Les Blues");
        mockTeamWithPlayers.setWorldRanking(1);
        mockTeamWithPlayers.setPool("A");
        mockTeamWithPlayers.setCountry("France");

        List<PlayersForTeamDto> players = new ArrayList<>();
        PlayersForTeamDto player1 = new PlayersForTeamDto();
        player1.setFullname("Player 1");
        player1.setPosition(Position.ATTACKER);
        player1.setValue(1000);
        player1.setRating(80);
        players.add(player1);

        PlayersForTeamDto player2 = new PlayersForTeamDto();
        player2.setFullname("Player 2");
        player2.setPosition(Position.DEFENDER);
        player2.setValue(900);
        player2.setRating(75);
        players.add(player2);

        mockTeamWithPlayers.setPlayers(players);
    }

    @Test
    void postPlayerSuccess() throws Exception {
        // Configurar el comportamiento esperado del servicio
        when(playerService.save(any(PostPlayerDto.class)))
                .thenReturn(ResponseEntity.ok(mockPlayerResponse));

        // Realizar la petición POST
        MvcResult result = mockMvc.perform(post("/rugby/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockPost)))
                .andExpect(status().isOk())
                .andReturn();

        // Verificar la respuesta
        String responseBody = result.getResponse().getContentAsString();
        ResponsePlayerDto actualResponse = objectMapper.readValue(responseBody, ResponsePlayerDto.class);

        assertEquals(mockPlayerResponse.getEquipoId(), actualResponse.getEquipoId());
        assertEquals(mockPlayerResponse.getTeamName(), actualResponse.getTeamName());
        assertEquals(mockPlayerResponse.getFullname(), actualResponse.getFullname());
    }

    @Test
    void postPlayerWhenTeamNotFound() throws Exception {
        // Simular que el servicio devuelve null (equipo no encontrado)
        when(playerService.save(any(PostPlayerDto.class)))
                .thenReturn(null);

        mockMvc.perform(post("/rugby/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockPost)))
                .andExpect(status().isBadGateway());
    }

    @Test
    void getTeamWithPlayersSuccess() throws Exception {
        // Configurar el comportamiento esperado del servicio
        when(playerService.getPlayersOfTeamById(1))
                .thenReturn(ResponseEntity.ok(mockTeamWithPlayers));

        // Realizar la petición GET
        MvcResult result = mockMvc.perform(get("/rugby/teams/{equipoId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Les Blues"))
                .andExpect(jsonPath("$.worldRanking").value(1))
                .andExpect(jsonPath("$.pool").value("A"))
                .andExpect(jsonPath("$.country").value("France"))
                .andExpect(jsonPath("$.players").isArray())
                .andExpect(jsonPath("$.players", hasSize(2)))
                .andExpect(jsonPath("$.players[0].fullname").value("Player 1"))
                .andExpect(jsonPath("$.players[0].rating").value(80))
                .andReturn();

        // Verificar que el servicio fue llamado
        verify(playerService, times(1)).getPlayersOfTeamById(1);
    }

    @Test
    void getTeamWithPlayersNotFound() throws Exception {
        // Configurar el comportamiento esperado del servicio cuando no encuentra el equipo
        when(playerService.getPlayersOfTeamById(999))
                .thenReturn(null);

        // Realizar la petición GET
        mockMvc.perform(get("/rugby/teams/{equipoId}", 999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // Verificar que el servicio fue llamado
        verify(playerService, times(1)).getPlayersOfTeamById(999);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}