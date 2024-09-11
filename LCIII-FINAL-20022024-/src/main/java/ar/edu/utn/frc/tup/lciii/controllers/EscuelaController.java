package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.MateriaDto;
import ar.edu.utn.frc.tup.lciii.services.AlumnoService;
import ar.edu.utn.frc.tup.lciii.services.MateriaService;
import ar.edu.utn.frc.tup.lciii.services.impl.AlumnoServiceImpl;
import ar.edu.utn.frc.tup.lciii.services.impl.MateriaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gestion")
public class EscuelaController {

    private final MateriaService materiaService;
    private final AlumnoService alumnoService;

    public EscuelaController(MateriaService materiaService, AlumnoService alumnoService) {
        this.materiaService = materiaService;
        this.alumnoService = alumnoService;
    }
    @GetMapping()
    public List<MateriaDto> GetMateria() {
        return materiaService.getMaterias();
    }
    @PostMapping("/materias")
    public List<MateriaDto> AddMateria(MateriaDto materiaDto) {
        materiaService.saveMateria(materiaDto);
        return materiaService.getMaterias();
    }


}
