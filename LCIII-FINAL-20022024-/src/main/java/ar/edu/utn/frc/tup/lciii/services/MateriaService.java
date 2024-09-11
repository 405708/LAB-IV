package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.domain.Materia;
import ar.edu.utn.frc.tup.lciii.dtos.common.MateriaDto;

import java.util.List;

public interface MateriaService {
    List<MateriaDto> getMaterias();
    List<MateriaDto> saveMateria(MateriaDto materia);
}
