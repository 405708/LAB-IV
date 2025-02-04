package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.dtos.ApiExterna.DisponibilidadDTO;
import ar.edu.utn.frc.tup.lciv.dtos.ApiExterna.PrecioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RestClient {
    @Autowired
    private RestTemplate restTemplate;

    //String url = "http://localhost:8080/habitacion";
    String url = "http://final20231219-405708-reservas-1:8080/habitacion";


    public DisponibilidadDTO getDisponibilidad(Long idHotel, String tipo, String fecha) {
        Map<String, Object> response = restTemplate.getForObject(url +
                "/disponibilidad?hotel_id="+ idHotel +
                "&tipo_habitacion="+ tipo +"&fecha="+ fecha, Map.class);

        if (response != null) {
            return mapToDisponibilidad(response);
        } else {
            throw new IllegalArgumentException("No se encontró una disponibilidad con el ID proporcionado");
        }
    }

    private DisponibilidadDTO mapToDisponibilidad(Map<String, Object> dispData) {
        return DisponibilidadDTO.builder()
                .tipo_habitacion((String) dispData.get("tipo_habitacion"))
                .hotel_id(((Number) dispData.get("hotel_id")).longValue()) // Convertir a Long
                .fecha((String) dispData.get("fecha"))       // Obtener el nombre
                .disponible((Boolean) dispData.get("disponible"))
                .build();
    }

    public PrecioDTO getPrecio(Long idHotel, String tipo) {
        Map<String, Object> response = restTemplate.getForObject(url +
                "/precio?hotel_id="+ idHotel +
                "&tipo_habitacion="+ tipo, Map.class);

        if (response != null) {
            return mapToPrecio(response);
        } else {
            throw new IllegalArgumentException("No se encontró un precio con el ID proporcionado");
        }
    }

    private PrecioDTO mapToPrecio(Map<String, Object> precioData) {
        return PrecioDTO.builder()
                .id_hotel(((Number) precioData.get("id_hotel")).longValue())
                .tipo_habitacion((String) precioData.get("tipo_habitacion"))
                .precio(new BigDecimal(precioData.get("precio_lista").toString())) // Conversión segura
                .build();
    }


}
