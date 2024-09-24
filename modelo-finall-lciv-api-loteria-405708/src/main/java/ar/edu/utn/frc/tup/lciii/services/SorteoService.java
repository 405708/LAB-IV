package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.domains.Sorteo;
import ar.edu.utn.frc.tup.lciii.dtos.common.SorteoBetsDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.SorteoTotalsDto;

public interface SorteoService {
    SorteoBetsDto getByIdWithBet(SorteoBetsDto sorteoBets);
    SorteoTotalsDto getByIdTotals(SorteoTotalsDto sorteoTotals);
}
