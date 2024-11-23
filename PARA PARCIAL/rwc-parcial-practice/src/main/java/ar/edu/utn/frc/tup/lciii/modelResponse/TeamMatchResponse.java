package ar.edu.utn.frc.tup.lciii.modelResponse;

import lombok.Data;

@Data
public class TeamMatchResponse {
    private int id;
    private int points;
    private int tries;
    private int yellowCards;
    private int redCards;
}
