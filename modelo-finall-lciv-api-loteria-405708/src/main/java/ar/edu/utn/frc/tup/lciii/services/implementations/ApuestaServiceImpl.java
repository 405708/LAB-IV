package ar.edu.utn.frc.tup.lciii.services.implementations;

import ar.edu.utn.frc.tup.lciii.domains.Apuesta;
import ar.edu.utn.frc.tup.lciii.domains.NumeroSorteado;
import ar.edu.utn.frc.tup.lciii.domains.Resultado;
import ar.edu.utn.frc.tup.lciii.domains.Sorteo;
import ar.edu.utn.frc.tup.lciii.dtos.common.ApuestaDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.EndpointSorteoDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.SaveApuestaDto;
import ar.edu.utn.frc.tup.lciii.repositories.ApuestaRepository;
import ar.edu.utn.frc.tup.lciii.services.ApuestaService;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApuestaServiceImpl implements ApuestaService {

    private final ApuestaRepository apuestaRepository;
    private final ModelMapper modelMapper;
    RestTemplate restTemplate = new RestTemplate();

    public ApuestaServiceImpl(ApuestaRepository apuestaRepository, ModelMapper modelMapper) {
        this.apuestaRepository = apuestaRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public SaveApuestaDto save(ApuestaDto apuestaDto) {
        Apuesta apuesta = modelMapper.map(apuestaDto, Apuesta.class);
        String url = "http://localhost:8082/sorteos?fecha=" + apuesta.getFecha_sorteo();
        ResponseEntity<EndpointSorteoDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<EndpointSorteoDto>() {}
        );

        EndpointSorteoDto sorteo = response.getBody();

        for (NumeroSorteado sorteado : sorteo.getNumerosSorteados()){
            Integer numeroApuesta = Integer.parseInt(apuesta.getNumero());
            if(sorteado.getNumero().equals(numeroApuesta)){
                apuesta.setResultado(Resultado.GANADOR);
            }
        }
        if(apuesta.getResultado() != Resultado.GANADOR){
            apuesta.setResultado(Resultado.PERDEDOR);
        }
        apuestaRepository.save(apuesta);

        return modelMapper.map(apuesta, SaveApuestaDto.class);
    }
}
