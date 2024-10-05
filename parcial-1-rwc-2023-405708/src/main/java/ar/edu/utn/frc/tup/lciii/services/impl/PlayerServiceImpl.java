package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.domains.Player;
import ar.edu.utn.frc.tup.lciii.domains.Position;
import ar.edu.utn.frc.tup.lciii.dtos.common.*;
import ar.edu.utn.frc.tup.lciii.repositories.PlayerRepository;
import ar.edu.utn.frc.tup.lciii.services.PlayerService;
import ar.edu.utn.frc.tup.lciii.services.RestClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final String RESILIENCE4J_INSTANCE_NAME = "circuitBreakerParcial";
    private static final String FALLBACK_METHOD = "fallback";

    private final PlayerRepository repository;
    private final ModelMapper modelMapper;

    @Autowired
    private RestClient restClient;

    public PlayerServiceImpl(PlayerRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }


    public ResponseEntity<String> fallback(Exception ex) {
        return ResponseEntity.status(503).body("Response from Circuit Breaker.");
    }

    @Override
    @CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME, fallbackMethod = FALLBACK_METHOD)
    public ResponseEntity<ResponsePlayerDto> save(PostPlayerDto postPlayerDto) {
        ResponsePlayerDto response = new ResponsePlayerDto();

        Player player = new Player();
        player.setFullname(postPlayerDto.getFullname());
        player.setShot(postPlayerDto.getShot());
        player.setSkills(postPlayerDto.getSkills());
        player.setSprint(postPlayerDto.getSprint());
        player.setPlayerValue(postPlayerDto.getValue());
        if(postPlayerDto.getPosition().equals("MC")){
            player.setPosition(Position.MIDFIELDER);
        }
        else if(postPlayerDto.getPosition().equals("DC")){
            player.setPosition(Position.ATTACKER);
        } else if (postPlayerDto.getPosition().equals("DF")) {
            player.setPosition(Position.DEFENDER);
        }
        else player.setPosition(Position.GOALKEEPER);

        ResponseEntity<EndpointTeamsDto> teamById = restClient.getTeamById(postPlayerDto.getEquipoId());

        if(teamById == null){
            return null;
        }

        System.out.println(teamById.getBody());

        if(teamById.getBody() != null){
            player.setEquipoId(postPlayerDto.getEquipoId());
        }
        else return null;

        EndpointTeamsDto team = teamById.getBody();

        Integer rating = (player.getSprint() + player.getShot() + player.getSkills()) / 3;

        repository.save(player);

        response.setEquipoId(player.getEquipoId());
        response.setTeamName(team.getName());
        response.setPosition(player.getPosition());
        response.setFullname(player.getFullname());
        response.setValue(player.getPlayerValue());
        response.setRating(rating);

        return ResponseEntity.ok(response);
    }

    @Override
    @CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME, fallbackMethod = FALLBACK_METHOD)
    public ResponseEntity<TeamsPlayersDto> getPlayersOfTeamById(Integer id) {

        ResponseEntity<EndpointTeamsDto> plTeam = restClient.getTeamById(id);

        EndpointTeamsDto team = plTeam.getBody();

        TeamsPlayersDto teamWithPlayers = new TeamsPlayersDto();
        teamWithPlayers.setId(team.getId());
        teamWithPlayers.setName(team.getName());
        teamWithPlayers.setWorldRanking(team.getWorldRanking());
        teamWithPlayers.setPool(team.getPool());
        teamWithPlayers.setCountry(team.getCountry());

        List<Player> players = repository.getPlayersByEquipoId(id);

        List<PlayersForTeamDto> playersForTeam = new ArrayList<>();

        for(Player p : players){
            PlayersForTeamDto newP = new PlayersForTeamDto();
            newP.setPosition(p.getPosition());
            newP.setFullname(p.getFullname());
            newP.setValue(p.getPlayerValue());
            Integer rating = (p.getSprint() + p.getShot() + p.getSkills()) / 3;
            newP.setRating(rating);
            playersForTeam.add(newP);
        }
        teamWithPlayers.setPlayers(playersForTeam);

        return ResponseEntity.ok(teamWithPlayers);
    }

    @Override
    @CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME, fallbackMethod = FALLBACK_METHOD)
    public ResponseEntity<TeamsPlayersDto> getPlayersOfTeamByCountry(String country) {
        return null;
    }

}
