package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.dtos.*;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public interface EleccionesService {
    List<DistritoDTO> getDistritosAllOrByName(String name);
    DistritoDTO getDistritoById(Long id);
    List<SeccionDTO> getSeccionesByDistrito(Long id);
    SeccionDTO getSeccionByIdAndDistritoId(Long distritoId, Long seccionId);
    List<CargoDTO> getCargosDistrito(Long id);
    CargoDTO getCargoByIdAndDistritoId(Long distritoId, Long cargoId);
    ResultadoDistritoDTO getResultadosByDistrito(Long id);
    ResultadosDTO getResultadosGenerales();
}
