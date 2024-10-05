package ar.edu.utn.frc.tup.lciii.dtos.common;

import lombok.Data;

@Data
public class EndpointTeamsDto {
    private Integer id;
    private String name;
    private String country;
    private Integer worldRanking;
    private String pool;
}
