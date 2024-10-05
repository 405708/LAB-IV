package ar.edu.utn.frc.tup.lciii.dtos.common;

import lombok.Data;

import java.util.List;

@Data
public class TeamsPlayersDto {
    private Integer id;
    private String name;
    private String country;
    private Integer worldRanking;
    private String pool;
    private List<PlayersForTeamDto> players;
}
