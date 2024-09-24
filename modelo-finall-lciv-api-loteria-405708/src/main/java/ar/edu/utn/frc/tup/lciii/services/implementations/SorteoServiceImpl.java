package ar.edu.utn.frc.tup.lciii.services.implementations;

import ar.edu.utn.frc.tup.lciii.dtos.common.SorteoBetsDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.SorteoTotalsDto;
import ar.edu.utn.frc.tup.lciii.repositories.SorteoRepository;
import ar.edu.utn.frc.tup.lciii.services.SorteoService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SorteoServiceImpl implements SorteoService {

    private final SorteoRepository sorteoRepository;
    private final ModelMapper modelMapper;
    RestTemplate restTemplate = new RestTemplate();

    public SorteoServiceImpl(SorteoRepository sorteoRepository, ModelMapper modelMapper) {
        this.sorteoRepository = sorteoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public SorteoBetsDto getByIdWithBet(SorteoBetsDto sorteoBets) {
        return null;
    }

    @Override
    public SorteoTotalsDto getByIdTotals(SorteoTotalsDto sorteoTotals) {
        return null;
    }
}
