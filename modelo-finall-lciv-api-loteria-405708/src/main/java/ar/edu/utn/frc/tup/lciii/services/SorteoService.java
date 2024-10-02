package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.common.EndpointSorteoDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.SorteoBetsDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.SorteoTotalsDto;

import java.util.List;

public interface SorteoService {
    SorteoBetsDto getByIdWithBet(Integer id);
    List<EndpointSorteoDto>  obtenerSorteos();
    SorteoTotalsDto getByIdTotals(SorteoTotalsDto sorteoTotals);
}
