package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.client.HttpClient;
import ar.edu.utn.frc.tup.lciii.modelResponse.MatchResponse;
import ar.edu.utn.frc.tup.lciii.modelResponse.TeamCountryResponse;
import ar.edu.utn.frc.tup.lciii.modelResponse.TeamMatchResponse;
import ar.edu.utn.frc.tup.lciii.models.Pool;
import ar.edu.utn.frc.tup.lciii.models.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PoolService {

    private final HttpClient httpClient;
    private final String url = "https://my-json-server.typicode.com/LCIV-2023/fake-api-rwc2023/matches";
    public List<Pool> getPools(String pool) {
        //basicamente hace la peticion y si el metodo tiene como parametro una pool filtra los matches de esa pool
        //sino devuelve todos los matches y calcula todas las standings posibles de todas las pools
        ResponseEntity<List<MatchResponse>> response =
                httpClient.get(url, new ParameterizedTypeReference<List<MatchResponse>>() {});
        if (response.getBody() != null) {
            List<MatchResponse> matches = response.getBody();
            if (pool != null && !pool.isEmpty()) {
                matches.removeIf(matchResponse -> !matchResponse.getPool().equalsIgnoreCase(pool));
            }
            return calculateStandings(matches);
        } else {
            return null;
        }
    }

    private List<Pool> calculateStandings(List<MatchResponse> matches) {
        Map<String, List<Team>> poolTeamsMap = new HashMap<>();
        List<TeamCountryResponse> teamsCountry = getTeams();
        for (MatchResponse match : matches) {
            String poolName = match.getPool();
            List<TeamMatchResponse> teamMatchResponses = match.getTeams();

            //crea una nueva entrie en el map si la pool todavia no existe.
            if (!poolTeamsMap.containsKey(poolName)) {
                poolTeamsMap.put(poolName, new ArrayList<>());
            }

            TeamMatchResponse team1 = teamMatchResponses.get(0);
            TeamMatchResponse team2 = teamMatchResponses.get(1);

            //Agarra los teams de la pool y los guarda en una lista si es que todavia no existen en la pool.
            //si ya existen los teams en la pool, los busca y los guarda en teamA y teamB
            Team teamA = findOrCreateTeam(poolTeamsMap.get(poolName), team1.getId(),teamsCountry);
            Team teamB = findOrCreateTeam(poolTeamsMap.get(poolName), team2.getId(),teamsCountry);
             //TODO REFACTORIZAR TODO EN UN UPDATE TEAM STATS
            //TODO GENERALIZAR
            boolean isDraw = team1.getPoints() == team2.getPoints();
            boolean isTeamAWinner = team1.getPoints() > team2.getPoints();
            boolean isTeamBWinner = team2.getPoints() > team1.getPoints();

            updateTeamStats(teamA, team1, isTeamAWinner, isDraw,team2.getPoints());
            updateTeamStats(teamB, team2, isTeamBWinner, isDraw,team1.getPoints());
        }

        return convertMapToPools(poolTeamsMap);
    }

    //Obtiene los teams de la API una sola vez para no pegarle 19milquinientas veces
    // a la misma API para setear el nombre y country
    private List<TeamCountryResponse> getTeams() {
        ResponseEntity<List<TeamCountryResponse>> teams = httpClient.get("https://my-json-server.typicode.com/LCIV-2023/fake-api-rwc2023/teams",
                new ParameterizedTypeReference<List<TeamCountryResponse>>() {});
        return teams.getBody();
    }

    //Busca el team en la lista de teams de la pool, si no lo encuentra lo crea y lo agrega a la lista
    private Team findOrCreateTeam(List<Team> teams, int teamId,List<TeamCountryResponse> teamCountryResponses) {
        for (Team team : teams) {
            if (team.getTeamId() == teamId) {
                return team;
            }
        }
        Team newTeam = initTeam();
        newTeam.setTeamId(teamId);
        for(TeamCountryResponse teamCountryResponse : teamCountryResponses){
            if(teamCountryResponse.getId() == teamId){
                newTeam.setTeamName(teamCountryResponse.getName());
                newTeam.setCountry(teamCountryResponse.getCountry());
            }
        }
        teams.add(newTeam);
        return newTeam;
    }


    //Actualiza las estadisticas del team en base al resultado del partido
    private void updateTeamStats(Team team, TeamMatchResponse teamMatchResponse, boolean isWinner, boolean isDraw, int pointsTeamContrary) {
        team.setMatchesPlayed(team.getMatchesPlayed() + 1);
        team.setPointsFor(team.getPointsFor() + teamMatchResponse.getPoints());
        team.setTriesMade(team.getTriesMade() + teamMatchResponse.getTries());
        team.setTotalYellowCards(team.getTotalYellowCards() + teamMatchResponse.getYellowCards());
        team.setTotalRedCards(team.getTotalRedCards() + teamMatchResponse.getRedCards());
        team.setPointsAgainst(team.getPointsAgainst() + pointsTeamContrary);
        if (isWinner) {
            team.setWins(team.getWins() + 1);
            team.setPoints(team.getPoints() + 4);
        } else if (isDraw) {
            team.setDraws(team.getDraws() + 1);
            team.setPoints(team.getPoints() + 2);
        } else {
            team.setLosses(team.getLosses() + 1);
        }
        if (teamMatchResponse.getTries() >= 4) {
            team.setBonusPoints(team.getBonusPoints() + 1);
        }
        if (!isWinner && !isDraw && (teamMatchResponse.getPoints() - pointsTeamContrary) <= 7) {
            team.setBonusPoints(team.getBonusPoints() + 1);
        }

        team.setPointsDifferential(team.getPointsFor() - team.getPointsAgainst());
    }

    // convierte el mapa clave (poolId) valor (lista de teams)
    // a una lista de pools, permitiendo devolver la response que defini en models
    private List<Pool> convertMapToPools(Map<String, List<Team>> poolTeamsMap) {
        List<Pool> pools = new ArrayList<>();
        for (Map.Entry<String, List<Team>> entry : poolTeamsMap.entrySet()) {
            Pool pool = new Pool();
            pool.setPoolId(entry.getKey());
            pool.setTeams(entry.getValue());
            pools.add(pool);
        }
        return pools;

    }

    private Team initTeam() {
        Team team = new Team();
        team.setTeamName("");
        team.setMatchesPlayed(0);
        team.setWins(0);
        team.setDraws(0);
        team.setLosses(0);
        team.setPointsFor(0);
        team.setPointsAgainst(0);
        team.setPointsDifferential(0);
        team.setTriesMade(0);
        team.setBonusPoints(0);
        team.setPoints(0);
        team.setTotalYellowCards(0);
        team.setTotalRedCards(0);
        return team;
    }
}
