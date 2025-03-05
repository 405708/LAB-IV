package com.library.biblioteca.service.Impl;

import com.library.biblioteca.dto.ClienteDTO;
import com.library.biblioteca.enums.EstadoLibro;
import com.library.biblioteca.model.Libro;
import com.library.biblioteca.model.Registro;
import com.library.biblioteca.repository.LibroRepository;
import com.library.biblioteca.repository.RegistroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BibliotecaServiceImplTest {

    public BibliotecaServiceImpl bibliotecaService;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private RegistroRepository registroRepository;

    @Mock
    private RestTemplate restTemplate;

    Libro libro;
    Libro libro2;

    Registro registroMenos2;
    Registro registroMenos5;
    Registro registroMas5;

    ClienteDTO cliente;

    List<String> isbns;
    List<Libro> libros;
    List<Registro> registros;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bibliotecaService = new BibliotecaServiceImpl(libroRepository, registroRepository ,restTemplate);

        libro = new Libro();
        libro.setTitulo("Libro 1");
        libro.setAutor("Libro Autor");
        libro.setEstado(EstadoLibro.DISPONIBLE);
        libro.setIsbn("1234567890");
        libro.setId(1L);

        libro2 = new Libro();
        libro2.setTitulo("Libro 2");
        libro2.setAutor("Libro Autor");
        libro2.setEstado(EstadoLibro.DISPONIBLE);
        libro2.setIsbn("987654321");
        libro.setId(2L);

        libros = new ArrayList<>();
        libros.add(libro);
        libros.add(libro2);

        //Faltan precios creo
        registroMenos2 = new Registro();
        registroMenos2.setId(1L);
        registroMenos2.setClienteId(1L);
        registroMenos2.setLibrosReservados(libros);
        registroMenos2.setFechaReserva(LocalDate.of(2020, 1, 1));
        registroMenos2.setFechaDevolucion(LocalDate.of(2020, 1, 2));
        registroMenos2.setTotal(BigDecimal.valueOf(200));

        registroMenos5 = new Registro();
        registroMenos2.setId(2L);
        registroMenos5.setClienteId(1L);
        registroMenos5.setLibrosReservados(libros);
        registroMenos5.setFechaReserva(LocalDate.of(2020, 1, 1));
        registroMenos5.setFechaDevolucion(LocalDate.of(2020, 1, 4));
        registroMenos5.setTotal(BigDecimal.valueOf(300));

        registroMas5 = new Registro();
        registroMenos2.setId(3L);
        registroMas5.setClienteId(1L);
        registroMas5.setLibrosReservados(libros);
        registroMas5.setFechaReserva(LocalDate.of(2020, 1, 1));
        registroMas5.setFechaDevolucion(LocalDate.of(2020, 1, 7));

        registros = new ArrayList<>();
        registros.add(registroMenos2);
        registros.add(registroMenos5);
        registros.add(registroMas5);


        isbns = new ArrayList<>();
        isbns.add("1234567890");
        isbns.add("987654321");

        cliente = new ClienteDTO();
        cliente.setId(1L);
        cliente.setNombre("Cliente 1");
        cliente.setDomicilio("Domicilio 1");


    }

    @Test
    void alquilarLibros() {
        when(libroRepository.findByIsbn("1234567890")).thenReturn(libros.get(0));
        when(libroRepository.findByIsbn("987654321")).thenReturn(libros.get(1));
        when(libroRepository.save(any(Libro.class))).thenReturn(libro);
        when(restTemplate.getForObject("http://localhost:8081/api/personas/aleatorio", ClienteDTO.class)).thenReturn(cliente).thenReturn(cliente);
        when(registroRepository.save(any(Registro.class))).thenReturn(registroMenos2);

        Registro registroDev = bibliotecaService.alquilarLibros(isbns);

        assertNotNull(registroDev);
        assertEquals(registroMenos2, registroDev);

        verify(libroRepository,times(1)).findByIsbn("1234567890");
        verify(libroRepository,times(1)).findByIsbn("987654321");
        verify(libroRepository,times(2)).save(any(Libro.class));
        verify(registroRepository,times(1)).save(any(Registro.class));

    }

    @Test
    void alquilarLibrosReservados() {
        Libro libroGta = new Libro();
        libroGta.setTitulo("Libro Gta");
        libroGta.setAutor("Libro Autor");
        libroGta.setIsbn("123");
        libroGta.setEstado(EstadoLibro.RESERVADO);
        libroGta.setId(3L);

        when(libroRepository.findByIsbn("123")).thenReturn(libroGta);

        assertThrows(IllegalStateException.class,() -> bibliotecaService.alquilarLibros(List.of("123")));
    }


    @Test
    void devolverLibros() {
        Long id = 1L;

        when(registroRepository.findById(id)).thenReturn(Optional.of(registroMenos2));
        when(libroRepository.save(any(Libro.class))).thenReturn(libro);
        when(registroRepository.save(any(Registro.class))).thenReturn(registroMenos2);

        Registro registro = bibliotecaService.devolverLibros(id);

        assertNotNull(registro);
        assertEquals(registroMenos2, registro);
        verify(registroRepository,times(1)).save(any(Registro.class));
        verify(libroRepository,times(2)).save(any(Libro.class));
        verify(registroRepository,times(2)).findById(id);

    }

    @Test
    void verTodosLosAlquileres() {
        when(registroRepository.findAll()).thenReturn(registros);

        List<Registro> registroList = bibliotecaService.verTodosLosAlquileres();

        assertNotNull(registroList);
        assertEquals(registros, registroList);
        verify(registroRepository,times(1)).findAll();

    }

    @Test
    void informeSemanal() {
        when(registroRepository.obtenerRegistrosSemana(LocalDate.now(), LocalDate.now().plus(7, ChronoUnit.DAYS))).thenReturn(registros);

        List<Registro> registroList = bibliotecaService.informeSemanal(LocalDate.now());

        assertNotNull(registroList);
        assertEquals(registros, registroList);

    }


    @Test
    void informeLibrosMasAlquiladosTest() {
        List<Object[]> objects = new ArrayList<>();

        objects.add(null);

        when(registroRepository.obtenerLibrosMasAlquilados()).thenReturn(objects);

        List<Object[]> result = bibliotecaService.informeLibrosMasAlquilados();

        assertNull(result.get(0));
    }
}