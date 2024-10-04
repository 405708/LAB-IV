package ar.edu.utn.frc.tup.lciii.services.implementations;

import ar.edu.utn.frc.tup.lciii.domains.Apuesta;
import ar.edu.utn.frc.tup.lciii.domains.Resultado;
import ar.edu.utn.frc.tup.lciii.dtos.common.ApuestaDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.EndpointSorteoDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.SaveApuestaDto;
import ar.edu.utn.frc.tup.lciii.repositories.ApuestaRepository;
import ar.edu.utn.frc.tup.lciii.services.ApuestaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ApuestaServiceImpl implements ApuestaService {

    private final ApuestaRepository apuestaRepository;
    private final ModelMapper modelMapper;

    @Autowired
    private RestTemplate restTemplate;
//    RestTemplate restTemplate = new RestTemplate();

    public ApuestaServiceImpl(ApuestaRepository apuestaRepository, ModelMapper modelMapper) {
        this.apuestaRepository = apuestaRepository;
        this.modelMapper = modelMapper;
    }



    @Override
    public SaveApuestaDto save(ApuestaDto apuestaDto) {
        System.out.println("ApuestaDto: " + apuestaDto);

//        Apuesta apuesta = modelMapper.map(apuestaDto, Apuesta.class);
        Apuesta apuesta = new Apuesta();
        apuesta.setFecha_sorteo(apuestaDto.getFecha_sorteo());
        apuesta.setId_cliente(apuestaDto.getId_cliente());
        apuesta.setNumero(apuestaDto.getNumero());
        apuesta.setMontoApostado(apuestaDto.getMontoApostado());

        System.out.println("Apuesta: " + apuesta);
        if (apuesta.getId_cliente() == null) {
            System.out.println("Error en el mapeo: id_cliente es null");
        }


        EndpointSorteoDto sorteoElegido = obtenerSorteo(apuesta.getFecha_sorteo());

        Integer numeroApuesta = Integer.parseInt(apuesta.getNumero());
        System.out.println(sorteoElegido);

//        Estimar pozo base de sorteo (10 pts)
//        El monto total del premio a entregar no podrá superar la reserva presente,
//        si sucede este escenario aplicar un
//        ajuste del 25% sobre el premio por cada 5 jugadores presentes en dicho sorteo.

//                Total acumulado en reserva (5 pts)
//        El monto acumulado por día es la resultante de la sumatoria de las apuestas
//        más la reserva existente y la posterior quita de los premios otorgados

        Integer maximoApuesta = sorteoElegido.getDineroTotalAcumulado() / 100;
        if(maximoApuesta > apuesta.getMontoApostado()){
            boolean numeroGanador = false;
            for (List<Integer> sorteado : sorteoElegido.getNumerosSorteados()){
                for (Integer numeroSorteado : sorteado) {
                    int coincidencias = contarCoincidencias(numeroSorteado, numeroApuesta);
                    System.out.println("Coincidencias: " + coincidencias);
                    switch (coincidencias) {
                        case 2:
                            apuesta.setPremio(apuesta.getMontoApostado() * 700);
                            apuesta.setResultado(Resultado.GANADOR);
                            numeroGanador = true;
                            break;
                        case 3:
                            apuesta.setPremio(apuesta.getMontoApostado() * 7000);
                            apuesta.setResultado(Resultado.GANADOR);
                            numeroGanador = true;
                            break;
                        case 4:
                            apuesta.setPremio(apuesta.getMontoApostado() * 60000);
                            apuesta.setResultado(Resultado.GANADOR);
                            numeroGanador = true;
                            break;
                        case 5:
                            apuesta.setPremio(apuesta.getMontoApostado() * 350000);
                            apuesta.setResultado(Resultado.GANADOR);
                            numeroGanador = true;
                            break;
                        default:
                            if (coincidencias > 1) {
                                // No hacer nada, ya que los casos están manejados arriba
                            } else {
                                apuesta.setResultado(Resultado.PERDEDOR);
                                apuesta.setPremio(0);
                            }
                    }
                    if (numeroGanador) {
                        break; // Salir del bucle interno si se encontró un ganador
                    }
                }
                if (numeroGanador) {
                    break; // Salir del bucle interno si se encontró un ganador
                }
            }
        }
        else{
            System.out.println("El dinero apostado es mayor al 1%");
        }
        apuestaRepository.save(apuesta);

        SaveApuestaDto saved = modelMapper.map(apuesta, SaveApuestaDto.class);
        saved.setId_sorteo(sorteoElegido.getNumeroSorteo());
        return saved;
    }

    @Override
    public EndpointSorteoDto obtenerSorteo(String fecha){
        //        Para el compose
//        String url = "http://loteria:8080/sorteos?fecha=" + fecha;
        //Para uso normal
        String url = "http://localhost:8082/sorteos?fecha=" + fecha;
        ResponseEntity<EndpointSorteoDto[]> response = restTemplate.getForEntity(url, EndpointSorteoDto[].class);

        if (response == null || response.getBody() == null || response.getBody().length == 0) {
            throw new RuntimeException("No se pudo obtener el sorteo para la fecha: " + fecha);
        }

        return response.getBody()[0];
    }

    public int contarCoincidencias(Integer numeroSorteado, Integer numeroApuesta) {
        String numSorteadoStr = String.valueOf(numeroSorteado);
        String numApuestaStr = String.valueOf(numeroApuesta);

        // Usamos un conjunto para contar los dígitos únicos
        Set<Character> sorteadoSet = new HashSet<>();
        for (char c : numSorteadoStr.toCharArray()) {
            sorteadoSet.add(c);
        }

        // Contamos coincidencias
        int coincidencias = 0;
        for (char c : numApuestaStr.toCharArray()) {
            if (sorteadoSet.contains(c)) {
                coincidencias++;
                // Para evitar contar el mismo dígito varias veces
                sorteadoSet.remove(c);
            }
        }

        return coincidencias;
    }
}
