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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class BibliotecaServiceImpl implements BibliotecaService {
    private final RegistroRepository registroRepository;
    private final LibroRepository libroRepository;
    private final RestTemplate restTemplate;

    private static final String URL = "http://localhost:8081";

    public BibliotecaServiceImpl(LibroRepository libroRepository, RegistroRepository registroRepository,  RestTemplate restTemplate) {
        this.registroRepository = registroRepository;
        this.libroRepository = libroRepository;
        this.restTemplate = restTemplate;
    }


    @Override
    public Registro alquilarLibros(List<String> isbns) {
        //TOD
        /**
         * Completar el metodo de alquiler
         * Se debe buscar la lista de libros por su codigo de isbn,
         * validar que los libros a alquilar tengan estado DISPONIBLE sino arrojar una exception
         * ya que solo se pueden alquilar libros que esten en dicho estado
         * throw new IllegalStateException("Uno o más libros ya están reservados.")
         * Recuperar un cliente desde la api externa /api/personas/aleatorio y guardar la reserva
         */

        List<Libro> librosReservados = new ArrayList<>();
        for (String isbn : isbns) {
            Libro libroExiste = libroRepository.findByIsbn(isbn);
            if (libroExiste != null) {
                if(libroExiste.getEstado().equals(EstadoLibro.RESERVADO)){
                    throw new IllegalStateException("Uno o más libros ya están reservados.");
                }
                libroExiste.setEstado(EstadoLibro.RESERVADO);
                libroRepository.save(libroExiste);
                librosReservados.add(libroExiste);
            }
        }

        ClienteDTO clienteDTO = restTemplate.getForObject(URL + "/api/personas/aleatorio", ClienteDTO.class);
        System.out.println(clienteDTO);
        if(clienteDTO == null){
            return null;
        }

        Registro registro = new Registro();
        registro.setFechaReserva(LocalDate.now());
        registro.setLibrosReservados(librosReservados);
        registro.setClienteId(clienteDTO.getId());
        registro.setNombreCliente(clienteDTO.getNombre());
        registro.setTotal(BigDecimal.ZERO);

        return registroRepository.save(registro);
    }

    @Override
    public Registro devolverLibros(Long registroId) {
        //TOD
        /**
         * Completar el metodo de devolucion
         * Se debe buscar la reserva por su id,
         * actualizar la fecha de devolucion y calcular el importe a facturar,
         * actualizar el estado de los libros a DISPONIBLE
         * y guardar el registro con los datos actualizados 
         */

        Registro registro = registroRepository.findById(registroId).isPresent() ? registroRepository.findById(registroId).get() : null;
        if(registro == null){
            throw new EntityNotFoundException("No existe la reserva con el id " + registroId);
        }
        registro.setFechaDevolucion(LocalDate.now());
        registro.setTotal(calcularCostoAlquiler(registro.getFechaReserva(), registro.getFechaDevolucion(), registro.getLibrosReservados().size()));

        for(Libro libro : registro.getLibrosReservados()){
            libro.setEstado(EstadoLibro.DISPONIBLE);
            libroRepository.save(libro);
        }

        return registroRepository.save(registro);
    }

    @Override
    public List<Registro> verTodosLosAlquileres() {
        return registroRepository.findAll();
    }

    // Cálculo de costo de alquiler
    private BigDecimal calcularCostoAlquiler(LocalDate inicio, LocalDate fin, int cantidadLibros) {
        //TOD
        /**
         * Completar el metodo de calculo
         * se calcula el importe a pagar por libro en funcion de la cantidad de dias,
         * es la diferencia entre el alquiler y la devolucion, respetando la siguiente tabla:
         * hasta 2 dias se debe pagar $100 por libro
         * desde 3 dias y hasta 5 dias se debe pagar $150 por libro
         * más de 5 dias se debe pagar $150 por libro + $30 por cada día extra
         */

        int diferencia = fin.compareTo(inicio);
        System.out.println(diferencia);

        int costoAlquiler = 0;

        if(diferencia <= 2){
            costoAlquiler = 100 * cantidadLibros;
        }
        else if(diferencia <= 5){
            costoAlquiler = 150 * cantidadLibros;
        }
        else{
            int diasExtra = diferencia - 5;
            costoAlquiler = (150 * cantidadLibros) + (30 * diasExtra);
        }

        return BigDecimal.valueOf(costoAlquiler);
    }

    @Override
    public List<Registro> informeSemanal(LocalDate fechaInicio) {
        //TOD
        /**
         * Completar el metodo de reporte semanal
         * se debe retornar la lista de registros de la semana tomando como referencia
         * la fecha de inicio para la busqueda
         */
        return registroRepository.obtenerRegistrosSemana(fechaInicio, fechaInicio.plus(7, ChronoUnit.DAYS));
    }

    @Override
    public List<Object[]> informeLibrosMasAlquilados() {
        //TOD
        /**
         * Completar el metodo de reporte de libros mas alquilados
         * se debe retornar la lista de libros mas alquilados
         */

        return registroRepository.obtenerLibrosMasAlquilados();
    }

}
