package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.domain.Alumno;
import ar.edu.utn.frc.tup.lciii.domain.Materia;
import ar.edu.utn.frc.tup.lciii.dtos.common.AlumnoDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.MateriaDto;
import ar.edu.utn.frc.tup.lciii.repository.AlumnoRepo;
import ar.edu.utn.frc.tup.lciii.services.AlumnoService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    private final AlumnoRepo alumnoRepo;
    private final MateriaServiceImpl materiaServiceImpl;
    private final ModelMapper modelMapper;

    public AlumnoServiceImpl(AlumnoRepo alumnoRepo, ModelMapper modelMapper, MateriaServiceImpl materiaServiceImpl) {
        this.alumnoRepo = alumnoRepo;
        this.modelMapper = modelMapper;
        this.materiaServiceImpl = materiaServiceImpl;
    }

    @Override
    public Alumno save(AlumnoDto alumno) {
        //http://localhost:8080/alumnos
        List<MateriaDto> allMaterias = materiaServiceImpl.getMaterias();
        alumno.setMateriaDto(allMaterias);

        Alumno savedAlumno = modelMapper.map(alumno, Alumno.class);

        return alumnoRepo.save(savedAlumno);
    }

    @Override
    public Alumno updateNotaAlumno(Alumno alumno) {
        return null;
    }

    @Override
    public Alumno getAlumnosByMateria(String materia) {
        return null;
    }
}
