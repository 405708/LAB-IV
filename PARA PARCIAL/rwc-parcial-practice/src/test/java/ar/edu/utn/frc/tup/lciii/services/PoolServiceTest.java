package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.client.HttpClient;
import ar.edu.utn.frc.tup.lciii.modelResponse.MatchResponse;
import ar.edu.utn.frc.tup.lciii.modelResponse.TeamCountryResponse;
import ar.edu.utn.frc.tup.lciii.modelResponse.TeamMatchResponse;
import ar.edu.utn.frc.tup.lciii.models.Pool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class PoolServiceTest {


    @MockBean
    HttpClient httpClient;

    @SpyBean
    PoolService poolService;

    private List<MatchResponse> matchResponses;
    private List<TeamCountryResponse> teamCountryResponses;

    @BeforeEach
    public void setUp() {
        TeamMatchResponse team1 = new TeamMatchResponse();
        team1.setId(1);
        team1.setPoints(10);
        team1.setTries(2);
        team1.setYellowCards(1);
        team1.setRedCards(0);

        TeamMatchResponse team2 = new TeamMatchResponse();
        team2.setId(2);
        team2.setPoints(15);
        team2.setTries(3);
        team2.setYellowCards(0);
        team2.setRedCards(1);

        MatchResponse match1 = new MatchResponse();
        match1.setId(1L);
        match1.setDate("2023-10-01");
        match1.setTeams(Arrays.asList(team1, team2));
        match1.setStadium(1);
        match1.setPool("A");

        MatchResponse match2 = new MatchResponse();
        match2.setId(2L);
        match2.setDate("2023-10-02");
        match2.setTeams(Arrays.asList(team1, team2));
        match2.setStadium(2);
        match2.setPool("B");

        matchResponses = new ArrayList<>();
        matchResponses.add(match1);
        matchResponses.add(match2);

        TeamCountryResponse teamCountry1 = new TeamCountryResponse();
        teamCountry1.setId(1);
        teamCountry1.setName("Team 1");
        teamCountry1.setCountry("Country 1");
        teamCountry1.setWorldRanking(1);
        teamCountry1.setPool("A");

        TeamCountryResponse teamCountry2 = new TeamCountryResponse();
        teamCountry2.setId(2);
        teamCountry2.setName("Team 2");
        teamCountry2.setCountry("Country 2");
        teamCountry2.setWorldRanking(2);
        teamCountry2.setPool("B");

        teamCountryResponses = new ArrayList<>();
        teamCountryResponses.add(teamCountry1);
        teamCountryResponses.add(teamCountry2);
    }

    @Test
    void getPoolsTest() {
        when(httpClient.get("https://my-json-server.typicode.com/LCIV-2023/fake-api-rwc2023/matches",
                new ParameterizedTypeReference<List<MatchResponse>>() {}))
                .thenReturn(ResponseEntity.ok(matchResponses));
        when(httpClient.get("https://my-json-server.typicode.com/LCIV-2023/fake-api-rwc2023/teams",
                new ParameterizedTypeReference<List<TeamCountryResponse>>() {}))
                .thenReturn(ResponseEntity.ok(teamCountryResponses));

        List<Pool> pools = poolService.getPools(null);
        assertNotNull(pools);
        assertEquals(2, pools.size());
        assertEquals("A", pools.get(0).getPoolId());
    }


}