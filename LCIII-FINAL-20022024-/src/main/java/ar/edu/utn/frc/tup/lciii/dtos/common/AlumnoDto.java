package ar.edu.utn.frc.tup.lciii.dtos.common;

import lombok.Data;

import java.util.List;

@Data
public class AlumnoDto {
    private String legajo;
    private String nombre;
    private List<MateriaDto> materiaDto;

}
