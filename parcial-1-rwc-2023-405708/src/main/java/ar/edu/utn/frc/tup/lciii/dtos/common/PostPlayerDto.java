package ar.edu.utn.frc.tup.lciii.dtos.common;

import ar.edu.utn.frc.tup.lciii.domains.Position;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class PostPlayerDto {
    private Integer equipoId;
    private String fullname;
    private String position;
    private Integer sprint;
    private Integer skills;
    private Integer shot;
    private Integer value;
}
