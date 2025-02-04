package com.library.biblioteca.service.Impl;


import com.library.biblioteca.dto.ClienteDTO;
import com.library.biblioteca.enums.EstadoLibro;
import com.library.biblioteca.model.Libro;
import com.library.biblioteca.model.Registro;
import com.library.biblioteca.repository.LibroRepository;
import com.library.biblioteca.repository.RegistroRepository;
import com.library.biblioteca.service.BibliotecaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BibliotecaServiceImpl implements BibliotecaService {
    private final RegistroRepository registroRepository;
    private final LibroRepository libroRepository;
    private final RestTemplate restTemplate;

    private static final String url = "http://localhost:8081";


    public BibliotecaServiceImpl(LibroRepository libroRepository, RegistroRepository registroRepository,  RestTemplate restTemplate) {
        this.registroRepository = registroRepository;
        this.libroRepository = libroRepository;
        this.restTemplate = restTemplate;
    }


    @Override
    public Registro alquilarLibros(List<String> isbns) {
        //TODO
        /**
         * Completar el metodo de alquiler
         * Se debe buscar la lista de libros por su codigo de isbn,
         * validar que los libros a alquilar tengan estado DISPONIBLE sino arrojar una exception
         * ya que solo se pueden alquilar libros que esten en dicho estado
         * throw new IllegalStateException("Uno o más libros ya están reservados.")
         * Recuperar un cliente desde la api externa /api/personas/aleatorio y guardar la reserva
         */

        List<Libro> libros = new ArrayList<>();

        for (String isbn : isbns) {
            Libro libro = libroRepository.findByIsbn(isbn);

            if (libro.getEstado().equals(EstadoLibro.DISPONIBLE)) {
                libros.add(libro);
            } else {
                throw new IllegalStateException("Uno o más libros ya están reservados.");
            }
        }


        ClienteDTO cliente = restTemplate.getForObject(url + "/api/personas/aleatorio", ClienteDTO.class);

        for (Libro libro : libros) {
            libro.setEstado(EstadoLibro.RESERVADO);
        }

        Registro registro = new Registro();

        registro.setLibrosReservados(libros);
        registro.setFechaReserva(LocalDate.now());
        registro.setClienteId(cliente.getId());
        registro.setNombreCliente(cliente.getNombre());

        return registroRepository.save(registro);
    }

    @Override
    public Registro devolverLibros(Long registroId) {
        //TODO
        /**
         * Completar el metodo de devolucion
         * Se debe buscar la reserva por su id,
         * actualizar la fecha de devolucion y calcular el importe a facturar,
         * actualizar el estado de los libros a DISPONIBLE
         * y guardar el registro con los datos actualizados 
         */

        Optional<Registro> optionalRegistro = registroRepository.findById(registroId);
        Registro registro;

        if (optionalRegistro.isPresent()) {
            registro = optionalRegistro.get();
            for (Libro libro : registro.getLibrosReservados()) {
                libro.setEstado(EstadoLibro.DISPONIBLE);
            }
        } else {
            throw new EntityNotFoundException();
        }

        registro.setFechaDevolucion(LocalDate.now());
        registro.setTotal(calcularCostoAlquiler(registro.getFechaReserva(),
                                                registro.getFechaDevolucion(),
                                                registro.getLibrosReservados().size()));

        return registroRepository.save(registro);
    }

    @Override
    public List<Registro> verTodosLosAlquileres() {
        return registroRepository.findAll();
    }

    // Cálculo de costo de alquiler
    private BigDecimal calcularCostoAlquiler(LocalDate inicio, LocalDate fin, int cantidadLibros) {
        //TODO
        /**
         * Completar el metodo de calculo
         * se calcula el importe a pagar por libro en funcion de la cantidad de dias,
         * es la diferencia entre el alquiler y la devolucion, respetando la siguiente tabla:
         * hasta 2 dias se debe pagar $100 por libro
         * desde 3 dias y hasta 5 dias se debe pagar $150 por libro
         * más de 5 dias se debe pagar $150 por libro + $30 por cada día extra
         */

        long days = ChronoUnit.DAYS.between(inicio, fin);

        if (days <= 2L) {
            return new BigDecimal(String.valueOf(100 * cantidadLibros));
        } else if (days <= 5L) {
            return new BigDecimal(String.valueOf(150 * cantidadLibros));
        } else {
            days -= 5;
            return new BigDecimal(String.valueOf((150 + 30 * days) * cantidadLibros));
        }
    }

    @Override
    public List<Registro> informeSemanal(LocalDate fechaInicio) {
        //TODO
        /**
         * Completar el metodo de reporte semanal
         * se debe retornar la lista de registros de la semana tomando como referencia
         * la fecha de inicio para la busqueda
         */
        return registroRepository.obtenerRegistrosSemana(fechaInicio, fechaInicio.plusDays(7L));
    }

    @Override
    public List<Object[]> informeLibrosMasAlquilados() {
        //TODO
        /**
         * Completar el metodo de reporte de libros mas alquilados
         * se debe retornar la lista de libros mas alquilados
         */ 
        return registroRepository.obtenerLibrosMasAlquilados();
    }

}
