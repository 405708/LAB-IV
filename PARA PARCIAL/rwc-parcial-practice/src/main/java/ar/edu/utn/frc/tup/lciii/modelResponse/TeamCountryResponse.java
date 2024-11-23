package ar.edu.utn.frc.tup.lciii.modelResponse;

import lombok.Data;


@Data
public class TeamCountryResponse {
    private int id;
    private String name;
    private String country;
    private int worldRanking;
    private String pool;
}
