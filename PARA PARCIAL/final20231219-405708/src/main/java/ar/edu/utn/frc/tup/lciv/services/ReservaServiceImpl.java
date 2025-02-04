package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.dtos.ApiExterna.DisponibilidadDTO;
import ar.edu.utn.frc.tup.lciv.dtos.ApiExterna.PrecioDTO;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.POSTReserva;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.Reserva;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.ReservaDTO;
import ar.edu.utn.frc.tup.lciv.repositories.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {

    @Autowired
    private RestClient restClient;

    @Autowired
    private ReservaRepository reservaRepository;

    @Override
    public ReservaDTO createReserva(POSTReserva reserva) {

        //Si la fecha es un string
//        LocalDate today = LocalDate.now();
//        LocalDate departureDate = LocalDateTime.parse(reserva.getFechaIngreso(), DateTimeFormatter.ISO_DATE_TIME).toLocalDate();
//
//        if (departureDate.isBefore(today)) {
//            throw new IllegalArgumentException("La fecha de salida no puede ser menor a hoy.");
//        }

        //Si la fecha ya es una fecha
        LocalDate today = LocalDate.now();
        LocalDate reservatedDate = reserva.getFechaIngreso().toLocalDate();
        LocalDate reservatedEndDate = reserva.getFechaSalida().toLocalDate();


        if (reservatedDate.isBefore(today)) {
            throw new IllegalArgumentException("La fecha de ingreso no puede ser menor a hoy.");
        }

        if(reservatedDate.isAfter(reservatedEndDate)) {
            throw new IllegalArgumentException("La fecha de salida no puede ser menor que la de ingreso.");
        }

        //Tipo de habitacion == SIMPLE DOBLE TRIPLE sino error
        List<String> tiposHabitacion = new ArrayList<>();
        tiposHabitacion.add("SIMPLE");
        tiposHabitacion.add("DOBLE");
        tiposHabitacion.add("TRIPLE");
        if(!tiposHabitacion.contains(reserva.getTipoHabitacion())) {
            throw new IllegalArgumentException("Los tipos de habitacion pueden ser SIMPLE-DOBLE-TRIPLE");
        }

        //TODO: Docker

        //TODO:Hacer un for por cada dia de la reserva para saber si esta disponible
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fecha = reserva.getFechaIngreso().format(formatter);

        DisponibilidadDTO disponibilidad = restClient.getDisponibilidad(reserva.getIdHotel(),reserva.getTipoHabitacion(),fecha);
        if(disponibilidad == null){
            if(disponibilidad.disponible = true){
                System.out.println("Disponibilidad encontrada");
            }
            else{
                System.out.println("Disponibilidad no encontrada");
                //Hacer algo
            }
        }

        Reserva reservaToSave = new Reserva();
        reservaToSave.setIdHotel(reserva.getIdHotel());
        reservaToSave.setIdCliente(reserva.getIdCliente());
        reservaToSave.setTipoHabitacion(reserva.getTipoHabitacion());
        reservaToSave.setFechaIngreso(reserva.getFechaIngreso());
        reservaToSave.setFechaSalida(reserva.getFechaSalida());
        reservaToSave.setMedioPago(reserva.getMedioPago());
        reservaToSave.setEstadoReserva("EXITOSA");



        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setIdHotel(reserva.getIdHotel());
        reservaDTO.setIdCliente(reserva.getIdCliente());
        reservaDTO.setTipoHabitacion(reserva.getTipoHabitacion());
        reservaDTO.setFechaIngreso(reserva.getFechaIngreso());
        reservaDTO.setFechaSalida(reserva.getFechaSalida());
        reservaDTO.setMedioPago(reserva.getMedioPago());




        //Calculo de precio segun temporada
        /*
        Temporada Alta (enero, febrero, julio, agosto): +30% al precio de lista.
        Temporada Baja (marzo, abril, octubre, noviembre): -10% al precio de lista.
        Temporada Media (junio, septiembre, diciembre): Precio de lista.
         */

        PrecioDTO precio = restClient.getPrecio(reserva.getIdHotel(), reserva.getTipoHabitacion());

        BigDecimal precioFinal = precio.getPrecio();

        // Definir los meses para cada grupo
        Set<Integer> grupo1 = Set.of(1, 2, 7, 8); // Enero, Febrero, Julio, Agosto
        Set<Integer> grupo2 = Set.of(3, 4, 10, 11); // Marzo, Abril, Octubre, Noviembre
        Integer mes = reserva.getFechaIngreso().getMonthValue();

        BigDecimal precioMostrar = BigDecimal.ZERO;
        if (grupo1.contains(mes)) {
            BigDecimal incremento = precioFinal.multiply(new BigDecimal("0.30"));
            precioMostrar = precioFinal.add(incremento);
        } else if (grupo2.contains(mes)) {
            BigDecimal decremento = precioFinal.multiply(new BigDecimal("0.10"));
            precioMostrar = precioFinal.subtract(decremento);
        } else {
            precioMostrar = precioFinal;
        }

        //Descuentos
        /*
        En efectivo (EFECTIVO): 25% de descuento sobre el precio segun temporada.
        En débito (TARJETA_DEBITO): 10% de descuento sobre el precio segun temporada.
        En credito (TARJETA_CREDITO) no se realiza descuento
         */

        // Aplicar descuentos según el medio de pago
        BigDecimal precioFinalFinal = BigDecimal.ZERO;

        if (reserva.getMedioPago().equals("EFECTIVO")) {
            BigDecimal desc = precioMostrar.multiply(new BigDecimal("0.25"));
            precioFinalFinal = precioMostrar.subtract(desc);
        } else if (reserva.getMedioPago().equals("TARJETA_DEBITO")) {
            BigDecimal desc = precioMostrar.multiply(new BigDecimal("0.10"));
            precioFinalFinal = precioMostrar.subtract(desc);
        } else {
            precioFinalFinal = precioMostrar;
        }

        reservaDTO.setPrecio(precioFinalFinal);
        reservaToSave.setPrecio(precioFinalFinal);

        //Setear ESTADO segun que pase
        reservaDTO.setEstadoReserva("EXITOSA");
        Reserva reserva1 = reservaRepository.save(reservaToSave);
        reservaDTO.setIdReserva(reserva1.getId());
        return reservaDTO;
    }

    @Override
    public ReservaDTO getReserva(Long id) {
        ReservaDTO reservaDTO = new ReservaDTO();
        Reserva reserva = reservaRepository.getReservaById(id);
        reservaDTO.setIdReserva(reserva.getId());
        reservaDTO.setIdHotel(reserva.getIdHotel());
        reservaDTO.setIdCliente(reserva.getIdCliente());
        reservaDTO.setTipoHabitacion(reserva.getTipoHabitacion());
        reservaDTO.setFechaIngreso(reserva.getFechaIngreso());
        reservaDTO.setFechaSalida(reserva.getFechaSalida());
        reservaDTO.setEstadoReserva(reserva.getEstadoReserva());
        reservaDTO.setPrecio(reserva.getPrecio());
        reservaDTO.setMedioPago(reserva.getMedioPago());

        return reservaDTO;
    }

}
