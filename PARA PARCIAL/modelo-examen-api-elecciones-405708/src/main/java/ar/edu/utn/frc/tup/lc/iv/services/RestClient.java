package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RestClient {

    @Autowired
    private RestTemplate restTemplate;

    String url = "http://localhost:8080/";
    //String url = "http://elecciones:8080/";
    public List<DistritoDTO> getAllDistritos(String name) {
        List<Map<String, Object>> response;
        if(name == null){
            response = restTemplate.getForObject(url + "distritos", List.class);
        }
        else {
             response = restTemplate.getForObject(url + "distritos?distritoNombre=" + name, List.class);
        }
        return response.stream().map(this::mapToDistritoDTO).collect(Collectors.toList());
    }
    public DistritoDTO getDistritoById(Long id) {
        // Realiza la solicitud y espera una lista
        List<Map<String, Object>> response = restTemplate.getForObject(url + "distritos?distritoId=" + id, List.class);

        // Verifica que la lista no esté vacía
        if (response != null && !response.isEmpty()) {
            // Mapea el primer elemento de la lista
            return mapToDistritoDTO(response.get(0));
        } else {
            throw new IllegalArgumentException("No se encontró un distrito con el ID proporcionado");
        }
    }

    private DistritoDTO mapToDistritoDTO(Map<String, Object> distritoData) {
        return DistritoDTO.builder()
                .id(((Number) distritoData.get("distritoId")).longValue()) // Convertir a Long
                .nombre((String) distritoData.get("distritoNombre"))       // Obtener el nombre
                .build();
    }

    /////////////////////////////////////////////
    public List<CargoDTO> getCargosById(Long id) {
        // Realiza la solicitud y espera una lista
        List<Map<String, Object>> response = restTemplate.getForObject(url + "cargos?distritoId=" + id, List.class);
        return response.stream().map(this::mapToCargoDTO).collect(Collectors.toList());
    }

    public CargoDTO getCargosByIdAndCargoId(Long id, Long cargoId) {
        // Realiza la solicitud y espera una lista
        List<Map<String, Object>> response = restTemplate.getForObject(url + "cargos?distritoId=" + id + "&cargoId=" + cargoId, List.class);
        // Verifica que la lista no esté vacía
        if (response != null && !response.isEmpty()) {
            // Mapea el primer elemento de la lista
            return mapToCargoDTO(response.get(0));
        } else {
            throw new IllegalArgumentException("No se encontró un cargo con el ID proporcionado");
        }
    }


    public CargoDTO mapToCargoDTO(Map<String, Object> cargoData) {
        return CargoDTO.builder()
                .id(((Number) cargoData.get("cargoId")).longValue()) // Convertir a Long
                .nombre((String) cargoData.get("cargoNombre"))       // Obtener el nombre
                .build();
    }


    ///////////////////////////////////////////////////////////////////

    public List<SeccionDTO> getSeccionById(Long id) {
        // Realiza la solicitud y espera una lista
        List<Map<String, Object>> response = restTemplate.getForObject(url + "secciones?distritoId=" + id, List.class);
        return response.stream().map(this::mapToSeccionDTO).collect(Collectors.toList());
    }

    public SeccionDTO getSeccionByIdAndDistritoId(Long id, Long seccionId) {
        // Realiza la solicitud y espera una lista
        List<Map<String, Object>> response = restTemplate.getForObject(url + "secciones?distritoId=" + id + "&seccionId=" + seccionId, List.class);
        // Verifica que la lista no esté vacía
        if (response != null && !response.isEmpty()) {
            // Mapea el primer elemento de la lista
            return mapToSeccionDTO(response.get(0));
        } else {
            throw new IllegalArgumentException("No se encontró una seccion con el ID proporcionado");
        }
    }


    public SeccionDTO mapToSeccionDTO(Map<String, Object> cargoData) {
        return SeccionDTO.builder()
                .id(((Number) cargoData.get("seccionId")).longValue()) // Convertir a Long
                .nombre((String) cargoData.get("seccionNombre"))       // Obtener el nombre
                .build();
    }



    ///////////////////////////////////////

    public List<ResultadoEndpointDTO> getResultadosByDistritoId(Long id) {
        // Realiza la solicitud y espera una lista
        List<Map<String, Object>> response = restTemplate.getForObject(url + "resultados?districtId=" + id, List.class);
        return response.stream().map(this::mapToResDTO).collect(Collectors.toList());
    }

    public ResultadoEndpointDTO mapToResDTO(Map<String, Object> data) {
        return ResultadoEndpointDTO.builder()
                .id(((Number) data.get("id")).longValue()) // Convertir a Long
                .distritoId(((Number) data.get("distritoId")).longValue())       // Obtener el nombre
                .seccionId(((Number) data.get("seccionId")).longValue())
                .cargoId(((Number) data.get("cargoId")).longValue())
                .agrupacionId(((Number) data.get("agrupacionId")).longValue())
                .votosTipo((String) data.get("votosTipo"))
                .votosCantidad((Integer) data.get("votosCantidad"))
                .build();
    }

    ////////////////////////////////////////////
    public AgrupacionDTO getAgrupacionById(Long id) {
        // Realiza la solicitud esperando un Map en lugar de una List
        Map<String, Object> response = restTemplate.getForObject(url + "agrupaciones/" + id, Map.class);

        // Verifica que la respuesta no sea null
        if (response != null) {
            return mapToAgrupacionDTO(response);
        } else {
            throw new IllegalArgumentException("No se encontró una agrupación con el ID proporcionado");
        }
    }

    public AgrupacionDTO mapToAgrupacionDTO(Map<String, Object> agrupacionData) {
        return AgrupacionDTO.builder()
                .id(((Number) agrupacionData.get("agrupacionId")).longValue())
                .nombre((String) agrupacionData.get("agrupacionNombre"))
                .build();
    }






}
