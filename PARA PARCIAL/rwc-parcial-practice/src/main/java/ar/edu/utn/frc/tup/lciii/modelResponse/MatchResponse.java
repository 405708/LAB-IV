package ar.edu.utn.frc.tup.lciii.modelResponse;

import lombok.Data;

import java.util.List;
@Data
public class MatchResponse {
    private Long id;
    private String date;
    private List<TeamMatchResponse> teams;
    private int stadium;
    private String pool;
}
