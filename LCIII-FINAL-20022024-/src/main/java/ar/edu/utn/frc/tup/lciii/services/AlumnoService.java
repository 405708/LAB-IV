package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.domain.Alumno;
import ar.edu.utn.frc.tup.lciii.dtos.common.AlumnoDto;

public interface AlumnoService {
    Alumno save(AlumnoDto alumno);
    Alumno updateNotaAlumno(Alumno alumno);
    Alumno getAlumnosByMateria(String materia);

}
