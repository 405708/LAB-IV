package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.domains.Player;
import ar.edu.utn.frc.tup.lciii.domains.Position;
import ar.edu.utn.frc.tup.lciii.dtos.common.*;
import ar.edu.utn.frc.tup.lciii.repositories.PlayerRepository;
import ar.edu.utn.frc.tup.lciii.services.PlayerService;
import ar.edu.utn.frc.tup.lciii.services.RestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class PlayerServiceImplTest {

    @InjectMocks
    @Spy
    private PlayerServiceImpl playerService;
    @Mock
    private PlayerRepository repository;
    @Mock
    private RestClient restClient;

    private PostPlayerDto postPlayerDto;
    private ResponsePlayerDto responsePlayerDto;
    private EndpointTeamsDto endpointTeamsDto;
    private PlayersForTeamDto playersForTeamDto;
    private TeamsPlayersDto teamsPlayersDto;
    private EndpointTeamsDto nullTeam = null;
    private final List<PlayersForTeamDto> EMPTY_PLAYERS = new ArrayList<>();

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        // Inicializar PostPlayerDto y ResponsePlayerDto con datos similares
        postPlayerDto = new PostPlayerDto();
        postPlayerDto.setFullname("pEDRO");
        postPlayerDto.setPosition("DC");
        postPlayerDto.setSprint(50);
        postPlayerDto.setSkills(50);
        postPlayerDto.setShot(50);
        postPlayerDto.setValue(1000);

        responsePlayerDto = new ResponsePlayerDto();
        responsePlayerDto.setEquipoId(postPlayerDto.getEquipoId());
        responsePlayerDto.setTeamName("Les Blues");
        responsePlayerDto.setFullname(postPlayerDto.getFullname());
        responsePlayerDto.setPosition(Position.ATTACKER);
        responsePlayerDto.setRating(50);
        responsePlayerDto.setValue(postPlayerDto.getValue());

        // Inicializar EndpointTeamsDto y TeamsPlayersDto
        endpointTeamsDto = new EndpointTeamsDto();
        endpointTeamsDto.setId(1);
        endpointTeamsDto.setName("Les Blues");
        endpointTeamsDto.setCountry("Francia");
        endpointTeamsDto.setWorldRanking(1);
        endpointTeamsDto.setPool("A");

        playersForTeamDto = new PlayersForTeamDto();
        playersForTeamDto.setFullname(responsePlayerDto.getFullname());
        playersForTeamDto.setPosition(Position.ATTACKER);
        playersForTeamDto.setRating(responsePlayerDto.getRating());
        playersForTeamDto.setValue(responsePlayerDto.getValue());

        teamsPlayersDto = new TeamsPlayersDto();
        teamsPlayersDto.setId(endpointTeamsDto.getId());
        teamsPlayersDto.setName(endpointTeamsDto.getName());
        teamsPlayersDto.setCountry(endpointTeamsDto.getCountry());
        teamsPlayersDto.setWorldRanking(endpointTeamsDto.getWorldRanking());
        teamsPlayersDto.setPool(endpointTeamsDto.getPool());
        teamsPlayersDto.setPlayers(Collections.singletonList(playersForTeamDto)); // Lista con un solo jugador
    }

    @Test
    void PostPlayerNull(){
        postPlayerDto.setEquipoId(999);
        when(restClient.getTeamById(999)).thenReturn(ResponseEntity.ok(nullTeam));

        assertNull(playerService.save(postPlayerDto));
        verify(restClient, times(1)).getTeamById(999);

    }

    @Test
    void PostPlayerNotNull(){
        postPlayerDto.setEquipoId(1);
        when(restClient.getTeamById(1)).thenReturn(ResponseEntity.ok(endpointTeamsDto));

        ResponseEntity<ResponsePlayerDto> response = playerService.save(postPlayerDto);

        Integer rating = (postPlayerDto.getSprint() + postPlayerDto.getShot() + postPlayerDto.getSkills()) / 3;

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postPlayerDto.getFullname(), response.getBody().getFullname());
        assertEquals(postPlayerDto.getValue(), response.getBody().getValue());
        assertEquals(rating, response.getBody().getRating());
        verify(restClient, times(1)).getTeamById(1);
    }

    @Test
    public void getPlayersByTeamId_No_Players() {

        when(restClient.getTeamById(1)).thenReturn(ResponseEntity.ok(endpointTeamsDto));

        teamsPlayersDto.setPlayers(EMPTY_PLAYERS);
        when(playerService.getPlayersOfTeamById(1)).thenReturn(ResponseEntity.ok(teamsPlayersDto));

        ResponseEntity<TeamsPlayersDto> teamResponse = playerService.getPlayersOfTeamById(1);

        assertEquals(1,teamResponse.getBody().getId());
        assertTrue(teamResponse.getBody().getPlayers().isEmpty());
        verify(restClient, times(1)).getTeamById(1);
    }

    @Test
    public void getPlayersByTeamId_With_Players() {

        when(restClient.getTeamById(1)).thenReturn(ResponseEntity.ok(endpointTeamsDto));

        teamsPlayersDto.setPlayers(Collections.singletonList(playersForTeamDto));
        when(playerService.getPlayersOfTeamById(1)).thenReturn(ResponseEntity.ok(teamsPlayersDto));

        ResponseEntity<TeamsPlayersDto> teamResponse = playerService.getPlayersOfTeamById(1);

        assertEquals(1,teamResponse.getBody().getId());
        assertFalse(teamResponse.getBody().getPlayers().isEmpty());
        verify(restClient, times(1)).getTeamById(1);
    }





    // Método helper para convertir un objeto a JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}