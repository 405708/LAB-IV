package ar.edu.utn.frc.tup.lciii.services.implementations;

import ar.edu.utn.frc.tup.lciii.domains.Apuesta;
import ar.edu.utn.frc.tup.lciii.dtos.common.ApuestaSorteoDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.EndpointSorteoDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.SorteoBetsDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.SorteoTotalsDto;
import ar.edu.utn.frc.tup.lciii.repositories.ApuestaRepository;
import ar.edu.utn.frc.tup.lciii.services.SorteoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class SorteoServiceImpl implements SorteoService {

    private final ApuestaRepository apuestaRepository;
    private final ModelMapper modelMapper;

    @Autowired
    private RestTemplate restTemplate;
//    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public SorteoServiceImpl(ApuestaRepository apuestaRepository, ModelMapper modelMapper) {
        this.apuestaRepository = apuestaRepository;
        this.modelMapper = modelMapper;

    }

    @Override
    public SorteoBetsDto getByIdWithBet(Integer id) {

        if(id == null){
            return null;
        }
        List<EndpointSorteoDto> sorteo = obtenerSorteos();
        EndpointSorteoDto sorteoElegido = new EndpointSorteoDto();
        for(EndpointSorteoDto sorteoDto : sorteo){
            if(sorteoDto.getNumeroSorteo().equals(id)){
                sorteoElegido = sorteoDto;
            }
        }

        SorteoBetsDto sorteoToShow = new SorteoBetsDto();
        sorteoToShow.setId_sorteo(sorteoElegido.getNumeroSorteo());
        sorteoToShow.setFecha_sorteo(sorteoElegido.getFecha());
        sorteoToShow.setTotalEnReserva(sorteoElegido.getDineroTotalAcumulado());

        //Setearle las apuestas
        List<Apuesta> apuestas = apuestaRepository.getApuestasByFechaSorteo(sorteoToShow.getFecha_sorteo());
        if(apuestas == null){
            System.out.println("No existe");
            return null;
        }
        List<ApuestaSorteoDto> apuestasToAdd = new ArrayList<ApuestaSorteoDto>();
        for (Apuesta apuesta : apuestas){

            ApuestaSorteoDto newApuesta = new ApuestaSorteoDto();
            newApuesta.setId_cliente(apuesta.getId_cliente());
            newApuesta.setNumero(apuesta.getNumero());
            newApuesta.setResultado(apuesta.getResultado());
            newApuesta.setMontoApostado(apuesta.getMontoApostado());
            newApuesta.setPremio(apuesta.getPremio());

            apuestasToAdd.add(newApuesta);
        }

        sorteoToShow.setApuestas(apuestasToAdd);

        return sorteoToShow;
    }

    @Override
    public List<EndpointSorteoDto> obtenerSorteos() {
        //Uso normal
//        String url = "http://localhost:8082/sorteos";

        //Uso Docker
        String url = "http://loteria:8080/sorteos";
        ResponseEntity<EndpointSorteoDto[]> response = restTemplate.getForEntity(url, EndpointSorteoDto[].class);
        if (response.getBody() != null) {
            return Arrays.asList(response.getBody());
        }
        return Collections.emptyList();
    }

    @Override
    public SorteoTotalsDto getByIdTotals(SorteoTotalsDto sorteoTotals) {
        return null;
    }
}
