package com.library.biblioteca.service.Impl;

import com.library.biblioteca.RandomDataForObject;
import com.library.biblioteca.dto.ClienteDTO;
import com.library.biblioteca.enums.EstadoLibro;
import com.library.biblioteca.model.Libro;
import com.library.biblioteca.model.Registro;
import com.library.biblioteca.repository.LibroRepository;
import com.library.biblioteca.repository.RegistroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BibliotecaServiceImplTest {

    @Mock
    private RegistroRepository registroRepository;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BibliotecaServiceImpl sut;

    private Registro registro;

    private List<Registro> registros;

    private Libro libro;

    private List<Libro> libros;
    @BeforeEach
    void setUp() throws IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        registros = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            registros.add(RandomDataForObject.generateRandomValue(new Registro()));
        }

        registro = RandomDataForObject.generateRandomValuesCascade(new Registro());


        libros = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            libros.add(RandomDataForObject.generateRandomValuesCascade(new Libro()));
        }

        libro = RandomDataForObject.generateRandomValuesCascade(new Libro());
    }

    @Test
    void alquilarLibrosTest() throws IllegalAccessException {
        List<String> isbns = new ArrayList<>();
        isbns.add("A");
        isbns.add("B");
        isbns.add("C");

        for (Libro libro : libros) {
            libro.setEstado(EstadoLibro.DISPONIBLE);
        }

        when(libroRepository.findByIsbn("A")).thenReturn(libros.get(0));
        when(libroRepository.findByIsbn("B")).thenReturn(libros.get(1));
        when(libroRepository.findByIsbn("C")).thenReturn(libros.get(2));

        when(restTemplate.getForObject("http://localhost:8081/api/personas/aleatorio", ClienteDTO.class))
            .thenReturn(RandomDataForObject.generateRandomValue(new ClienteDTO()));

        when(registroRepository.save(any())).thenReturn(registro);

        Registro result = sut.alquilarLibros(isbns);

        verify(registroRepository, times(1)).save(any());
    }

    @Test
    void alquilarLibrosTestNotFound() {
        List<String> isbns = new ArrayList<>();
        isbns.add("A");

        for (Libro libro : libros) {
            libro.setEstado(EstadoLibro.RESERVADO);
        }

        when(libroRepository.findByIsbn("A")).thenReturn(libros.get(0));

        assertThrows((IllegalStateException.class), () -> sut.alquilarLibros(isbns));
    }

    @Test
    void devolverLibrosTest() {
        when(registroRepository.findById(1L)).thenReturn(Optional.of(registro));
        when(registroRepository.save(any())).thenReturn(registro);

        Registro result = sut.devolverLibros(1L);

        assertEquals(registro.getId(), result.getId());
        assertEquals(registro.getFechaReserva(), result.getFechaReserva());
        assertEquals(registro.getClienteId(), result.getClienteId());
        assertNotNull(result.getTotal());
        verify(registroRepository, times(1)).save(any());
    }

    @Test
    void devolverLibrosTestNotFound() {
        when(registroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows((EntityNotFoundException.class), () -> sut.devolverLibros(1L));
    }

    @Test
    void verTodosLosAlquileresTest() {

        when(registroRepository.findAll()).thenReturn(registros);

        List<Registro> result = sut.verTodosLosAlquileres();

        assertNotNull(result.get(0));
        assertNotNull(result.get(1));
        assertNotNull(result.get(2));
        assertEquals(3, result.size());
    }

    @Test
    void informeSemanalTest() {
        LocalDate inicio = LocalDate.now();
        when(registroRepository
            .obtenerRegistrosSemana(inicio, inicio.plusDays(7L)))
            .thenReturn(registros);

        List<Registro> result = sut.informeSemanal(inicio);

        assertNotNull(result.get(0));
        assertNotNull(result.get(1));
        assertNotNull(result.get(2));
        assertEquals(3, result.size());
    }

    @Test
    void informeLibrosMasAlquiladosTest() {
        List<Object[]> objects = new ArrayList<>();

        objects.add(null);

        when(registroRepository.obtenerLibrosMasAlquilados()).thenReturn(objects);

        List<Object[]> result = sut.informeLibrosMasAlquilados();

        assertNull(result.get(0));
    }

    @Test
    void calcularCostoAlquilerTestCase1() {

        BigDecimal result = ReflectionTestUtils
            .invokeMethod(sut, "calcularCostoAlquiler", LocalDate.now(), LocalDate.now().plusDays(1L), 2);

        assertEquals(new BigDecimal("200"), result);
    }

    @Test
    void calcularCostoAlquilerTestCase2() {

        BigDecimal result = ReflectionTestUtils
                .invokeMethod(sut, "calcularCostoAlquiler", LocalDate.now(), LocalDate.now().plusDays(3L), 3);

        assertEquals(new BigDecimal("450"), result);
    }

    @Test
    void calcularCostoAlquilerTestCase3() {

        BigDecimal result = ReflectionTestUtils
                .invokeMethod(sut, "calcularCostoAlquiler", LocalDate.now(), LocalDate.now().plusDays(7L), 2);

        assertEquals(new BigDecimal("420"), result);
    }
}
