package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.domains.Apuesta;
import ar.edu.utn.frc.tup.lciii.dtos.common.ApuestaDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.SaveApuestaDto;

public interface ApuestaService {
    SaveApuestaDto save(ApuestaDto apuestaDto);
}
