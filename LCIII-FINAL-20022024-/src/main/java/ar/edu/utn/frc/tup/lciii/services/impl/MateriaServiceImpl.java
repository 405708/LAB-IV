package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.domain.Materia;
import ar.edu.utn.frc.tup.lciii.dtos.common.Docente;
import ar.edu.utn.frc.tup.lciii.dtos.common.MateriaDto;
import ar.edu.utn.frc.tup.lciii.repository.MateriaRepo;
import ar.edu.utn.frc.tup.lciii.services.MateriaService;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MateriaServiceImpl implements MateriaService {

    private final MateriaRepo materiaRepo;
    private final ModelMapper modelMapper;
    RestTemplate restTemplate = new RestTemplate();

    public MateriaServiceImpl(MateriaRepo materiaRepo, ModelMapper modelMapper) {
        this.materiaRepo = materiaRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<MateriaDto> getMaterias() {
        //Rest template
        String url = "http://localhost:8081/docentes";
        ResponseEntity<List<Docente>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Docente>>() {}
        );

        List<Docente> docentes = response.getBody();

        List<String> materias = docentes.stream()
                .map(Docente::getMateria)
                .collect(Collectors.toList());

        List<MateriaDto> materiasShow = new ArrayList<>();
        List<Materia> materiaDb = materiaRepo.findAll();
        for (Materia materia : materiaDb) {
            MateriaDto materiaDto = new MateriaDto(materia.getNombre());
            materiasShow.add(materiaDto);
        }

        for (String materia : materias) {
            MateriaDto materiaDto = new MateriaDto(materia);
            materiasShow.add(materiaDto);
        }

        return materiasShow;
    }
    @Override
    public List<MateriaDto> saveMateria(MateriaDto materiaDto) {
        Materia mat = modelMapper.map(materiaDto, Materia.class);
        mat.setNombre(materiaDto.getMateria());
        mat.setEstado("Pendiente");
        mat.setCalificacion(0);

        materiaRepo.save(mat);


        return getMaterias();
    }
}
