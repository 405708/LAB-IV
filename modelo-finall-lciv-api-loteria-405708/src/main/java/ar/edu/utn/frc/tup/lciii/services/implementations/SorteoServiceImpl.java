package ar.edu.utn.frc.tup.lciii.services.implementations;

import ar.edu.utn.frc.tup.lciii.domains.Apuesta;
import ar.edu.utn.frc.tup.lciii.dtos.common.ApuestaSorteoDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.EndpointSorteoDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.SorteoBetsDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.SorteoTotalsDto;
import ar.edu.utn.frc.tup.lciii.repositories.ApuestaRepository;
import ar.edu.utn.frc.tup.lciii.services.SorteoService;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class SorteoServiceImpl implements SorteoService {

    private final ApuestaRepository apuestaRepository;
    private final ModelMapper modelMapper;
    RestTemplate restTemplate = new RestTemplate();

    public SorteoServiceImpl(ApuestaRepository apuestaRepository, ModelMapper modelMapper) {
        this.apuestaRepository = apuestaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public SorteoBetsDto getByIdWithBet(Integer id) {

        String url = "http://localhost:8082/sorteos";
//        String url = "http://loteria:8080/sorteos;"
//        String responseBody = restTemplate.getForObject(url, String.class);
//        System.out.println("Respuesta cruda: " + responseBody);

        ResponseEntity<List<EndpointSorteoDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<EndpointSorteoDto>>() {}
        );

        List<EndpointSorteoDto>  sorteo =null;
        if (response.getBody() != null) {
            sorteo = response.getBody();
        } else {
            System.out.println("Error");
        }
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
    public SorteoTotalsDto getByIdTotals(SorteoTotalsDto sorteoTotals) {
        return null;
    }
}
