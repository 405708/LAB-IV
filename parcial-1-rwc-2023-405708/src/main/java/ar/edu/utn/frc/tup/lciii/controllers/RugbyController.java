package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.PostPlayerDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.ResponsePlayerDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.TeamsPlayersDto;
import ar.edu.utn.frc.tup.lciii.services.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rugby")
@CrossOrigin(origins = "*")
public class RugbyController {
    private final PlayerService service;
    public RugbyController(PlayerService service) {
        this.service = service;
    }

    @PostMapping("/player")
    public ResponseEntity<ResponsePlayerDto> POSTPlayer(@RequestBody PostPlayerDto player){
        ResponseEntity<ResponsePlayerDto>  result = service.save(player);
        if(result == null){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
        return result;
    }

    @GetMapping("/teams/{equipoId}")
    public ResponseEntity<TeamsPlayersDto> GetTeamsWithPlayers (@PathVariable("equipoId") Integer id){
        ResponseEntity<TeamsPlayersDto> team = service.getPlayersOfTeamById(id);
        if(team == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return team;
    }


}
