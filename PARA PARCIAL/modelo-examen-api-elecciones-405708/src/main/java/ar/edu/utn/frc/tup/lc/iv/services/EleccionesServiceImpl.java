package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.dtos.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EleccionesServiceImpl implements EleccionesService {

    //CircuitBreaker
    private static final String RESILIENCE4J_INSTANCE_NAME = "circuitBreakerParcial";
    private static final String FALLBACK_METHOD = "fallback";


    @Autowired
    private RestClient restClient;



    @Override
    public List<DistritoDTO> getDistritosAllOrByName(String name) {
        List<DistritoDTO> distritos = restClient.getAllDistritos(name);
        if(distritos.isEmpty()){
            return null;
        }
        return distritos;
    }

    @Override
    public DistritoDTO getDistritoById(Long id) {
        DistritoDTO distrito = restClient.getDistritoById(id);
        if(distrito == null){
            return null;
        }
        return distrito;
    }

    @Override
    public List<CargoDTO> getCargosDistrito(Long id) {
        List<CargoDTO> cargos = restClient.getCargosById(id);
        if(cargos.isEmpty()){
            return null;
        }
        return cargos;
    }

    @Override
    public CargoDTO getCargoByIdAndDistritoId(Long distritoId, Long cargoId) {
        CargoDTO cargo = restClient.getCargosByIdAndCargoId(distritoId, cargoId);
        if(cargo == null){
            return null;
        }
        return cargo;
    }

    @Override
    public List<SeccionDTO> getSeccionesByDistrito(Long id) {
        List<SeccionDTO> seccionDTOS = restClient.getSeccionById(id);
        if(seccionDTOS.isEmpty()){
            return null;
        }
        return seccionDTOS;
    }

    @Override
    public SeccionDTO getSeccionByIdAndDistritoId(Long distritoId, Long seccionId) {
        SeccionDTO seccionDTO = restClient.getSeccionByIdAndDistritoId(distritoId, seccionId);
        if(seccionDTO == null){
            return null;
        }
        return seccionDTO;
    }




    @Override
    @CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME, fallbackMethod = FALLBACK_METHOD)
    public ResultadoDistritoDTO getResultadosByDistrito(Long id) {
        List<ResultadoEndpointDTO> resultadosDefault = restClient.getResultadosByDistritoId(id);

        if(resultadosDefault.isEmpty()){
            return  null;
        }
        return mapToResultadoDistitoDTO(resultadosDefault);
    }
    // CircuitBreaker fallback
    public ResultadoDistritoDTO fallback(Long id, Throwable ex) {
        ResultadoDistritoDTO fallbackResult = new ResultadoDistritoDTO();
        fallbackResult.setId(id);
        fallbackResult.setResultadosAgrupaciones(Collections.emptyList());
        fallbackResult.setNombre("Fallback: No se pudieron obtener los resultados para el distrito.");
        return fallbackResult;
    }


    public ResultadoDistritoDTO mapToResultadoDistitoDTO(List<ResultadoEndpointDTO> resultados){

        //Distrito
        DistritoDTO distritoDTO = getDistritoById(resultados.get(0).getDistritoId());

        //Votantes
        Integer votantes = 0;
        for(ResultadoEndpointDTO resultadoEndpointDTO : resultados){
            votantes += resultadoEndpointDTO.getVotosCantidad();
        }

        //Secciones
        List<SeccionDTO> seccionDTOS = getSeccionesByDistrito(distritoDTO.getId());
        List<String> secciones = new ArrayList<>();
        for (SeccionDTO seccionDTO : seccionDTOS){
            secciones.add(seccionDTO.getNombre());
        }

        //Porcentaje Padron
        List<DistritoDTO> distritos = getDistritosAllOrByName(null);
        Integer padronNacional = calculatePadron(distritos);


        // Convertir Integer a BigDecimal
        BigDecimal votantesBD = new BigDecimal(votantes);
        BigDecimal padronBD = new BigDecimal(padronNacional);

        // Dividir y escalar el resultado a 4 decimales
        BigDecimal porcentajePadron = votantesBD.divide(padronBD, 4, RoundingMode.HALF_UP);


        //AgrupacionGanadora
//        List<ResultadoAgrupacionDTO> resultadosPorAgrupacion = resultadosAgrupaciones(resultados, votantes);
        List<ResultadoAgrupacionDTO> resultadosPorAgrupacion = resultadosMap(resultados, votantes);
        resultadosPorAgrupacion.sort(Comparator.comparing(ResultadoAgrupacionDTO::getVotos).reversed());
        Integer index = 0;
        for(ResultadoAgrupacionDTO res : resultadosPorAgrupacion){
            index = index + 1;
            res.setPosicion(index);
        }

        //ResultadoAgrupaciones

        ResultadoDistritoDTO resultadoDistritoDTO = new ResultadoDistritoDTO();
        resultadoDistritoDTO.setId(distritoDTO.getId());
        resultadoDistritoDTO.setNombre(distritoDTO.getNombre());
        resultadoDistritoDTO.setVotosEscrutados(votantes);
        resultadoDistritoDTO.setSecciones(secciones);
        resultadoDistritoDTO.setPorcentajePadronNacional(porcentajePadron);
        resultadoDistritoDTO.setAgrupacionGanadora(resultadosPorAgrupacion.get(0).getNombre());
        resultadoDistritoDTO.setResultadosAgrupaciones(resultadosPorAgrupacion);

        return resultadoDistritoDTO;
    }

    //TODO Circuit Breaker + Test
    public List<ResultadoAgrupacionDTO> resultadosMap(List<ResultadoEndpointDTO> resultsToMap, Integer votantes) {
        // Mapa de agrupaciones
        Map<Long, Integer> votosPorAgrupacion = new HashMap<>();

        // Mapa de Tipos de votos anulados
        Map<String, Integer> votosAnulados = new HashMap<>();

        for (ResultadoEndpointDTO res : resultsToMap) {
            if (res.getAgrupacionId() == 0) {
                // Actualizar votosAnulados usando merge
                votosAnulados.merge(res.getVotosTipo(), res.getVotosCantidad(), Integer::sum);
            } else {
                // Actualizar votosPorAgrupacion usando merge
                votosPorAgrupacion.merge(res.getAgrupacionId(), res.getVotosCantidad(), Integer::sum);
            }
        }

        // Convertir a ResultadoAgrupacionDTO
        List<ResultadoAgrupacionDTO> resultadosAgrupacionToReturn = new ArrayList<>();

        // Procesar votosPorAgrupacion
        votosPorAgrupacion.forEach((agrupacionId, votos) -> {
            BigDecimal porcentaje = BigDecimal.valueOf(votos)
                    .divide(BigDecimal.valueOf(votantes), 4, RoundingMode.HALF_UP);

            String porcentajeFormateado = porcentaje.multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP)
                    .toString() + "%";

            ResultadoAgrupacionDTO res = new ResultadoAgrupacionDTO();
            res.setNombre(nameAgrupacion(agrupacionId));
            res.setVotos(votos);
            res.setPorcentaje(porcentajeFormateado);
            resultadosAgrupacionToReturn.add(res);
        });

        // Procesar votosAnulados
        votosAnulados.forEach((votosTipo, votos) -> {
            BigDecimal porcentaje = BigDecimal.valueOf(votos)
                    .divide(BigDecimal.valueOf(votantes), 4, RoundingMode.HALF_UP);

            String porcentajeFormateado = porcentaje.multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP)
                    .toString() + "%";

            ResultadoAgrupacionDTO res = new ResultadoAgrupacionDTO();
            res.setNombre(votosTipo);
            res.setVotos(votos);
            res.setPorcentaje(porcentajeFormateado);
            resultadosAgrupacionToReturn.add(res);
        });

        return resultadosAgrupacionToReturn;
    }


    public String nameAgrupacion(Long id){
        AgrupacionDTO ag = restClient.getAgrupacionById(id);
        if(ag == null){
            return null;
        }
        return ag.getNombre();
    }

    public Integer calculatePadron(List<DistritoDTO> distritos){
        Integer padron = 0;
        for (DistritoDTO distrito : distritos) {
            List<ResultadoEndpointDTO> res = restClient.getResultadosByDistritoId(distrito.getId());
            for(ResultadoEndpointDTO resDto : res){
                padron += resDto.getVotosCantidad();
            }
        }
        return padron;
    }


    @Override
    @CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME, fallbackMethod = FALLBACK_METHOD)
    public ResultadosDTO getResultadosGenerales() {

        ResultadosDTO resultadoTotal = new ResultadosDTO();
        //Porcentaje Padron
        List<DistritoDTO> distritos = getDistritosAllOrByName(null);
        Integer padronNacional = calculatePadron(distritos);

        //Distritos
        List<String> distritosNacional = new ArrayList<>();
        for (DistritoDTO distritoDTO : distritos){
            distritosNacional.add(distritoDTO.getNombre());
        }

        //Resultados distritos
        List<ResultadoDistritoDTO> allResults = new ArrayList<>();
        for(DistritoDTO distritoDTO : distritos){
            ResultadoDistritoDTO res = getResultadosByDistrito(distritoDTO.getId());
            allResults.add(res);
        }

        //Agrupacion Ganadora

        //Resultados nacionales
        Map<String, Integer> votosporAgrupacion = new HashMap<>();
        allResults.forEach(distrito ->
                distrito.getResultadosAgrupaciones().forEach(resultado -> {
                        votosporAgrupacion.merge(
                                resultado.getNombre(),
                                resultado.getVotos(),
                                Integer::sum
                        );
                })
        );

        // Convertir a lista de ResultadoAgrupacionDTO
        List<ResultadoAgrupacionDTO> resultadosNacionales = votosporAgrupacion.entrySet().stream()
                .map(entry -> {
                    BigDecimal porcentaje = BigDecimal.valueOf(entry.getValue())
                            .divide(BigDecimal.valueOf(padronNacional), 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100));

                    return ResultadoAgrupacionDTO.builder()
                            .nombre(entry.getKey())
                            .votos(entry.getValue())
                            .porcentaje(porcentaje.setScale(2, RoundingMode.HALF_UP).toString() + "%")
                            .build();
                })
                .sorted(Comparator.comparing(ResultadoAgrupacionDTO::getVotos).reversed())
                .collect(Collectors.toList());

        // Asignar posiciones
        for (int i = 0; i < resultadosNacionales.size(); i++) {
            resultadosNacionales.get(i).setPosicion(i + 1);
        }

        // 4. Establecer agrupación ganadora
        if (!resultadosNacionales.isEmpty()) {
            resultadoTotal.setAgrupacionGanadora(resultadosNacionales.get(0).getNombre());
        }

        resultadoTotal.setVotosEscrutados(padronNacional);
        resultadoTotal.setDistritos(distritosNacional);
        resultadoTotal.setResultadoDistritos(allResults);
        resultadoTotal.setResultadosNacionales(resultadosNacionales);

        return resultadoTotal;
    }
    public ResultadosDTO fallback(Throwable ex) {
        ResultadosDTO fallbackResult = new ResultadosDTO();
        fallbackResult.setVotosEscrutados(0);
        fallbackResult.setDistritos(Collections.emptyList());
        fallbackResult.setResultadoDistritos(Collections.emptyList());
        fallbackResult.setResultadosNacionales(Collections.emptyList());
        fallbackResult.setAgrupacionGanadora("Fallback");
        return fallbackResult;
    }



}
