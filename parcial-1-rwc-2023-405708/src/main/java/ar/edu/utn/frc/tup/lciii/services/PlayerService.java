package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.common.EndpointTeamsDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.PostPlayerDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.ResponsePlayerDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.TeamsPlayersDto;
import org.springframework.http.ResponseEntity;

public interface PlayerService {
    ResponseEntity<ResponsePlayerDto>  save(PostPlayerDto postPlayerDto);
    ResponseEntity<TeamsPlayersDto>  getPlayersOfTeamById(Integer id);
    ResponseEntity<TeamsPlayersDto>  getPlayersOfTeamByCountry(String country);
}
