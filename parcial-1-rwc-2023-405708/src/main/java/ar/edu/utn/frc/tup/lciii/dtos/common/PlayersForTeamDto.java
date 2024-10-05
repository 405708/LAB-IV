package ar.edu.utn.frc.tup.lciii.dtos.common;

import ar.edu.utn.frc.tup.lciii.domains.Position;
import lombok.Data;

@Data
public class PlayersForTeamDto {
    private String fullname;
    private Position position;
    private Integer rating; //(Promedio entre V-R-T)
    private Integer value;
}
