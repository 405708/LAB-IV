package com.library.biblioteca.service.Impl;

import com.library.biblioteca.enums.EstadoLibro;
import com.library.biblioteca.model.Libro;
import com.library.biblioteca.model.Registro;
import com.library.biblioteca.repository.LibroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LibroServiceImplTest {

    public LibroServiceImpl libroServiceImpl;

    @Mock
    private LibroRepository libroRepository;


    Libro libro;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        libroServiceImpl = new LibroServiceImpl(libroRepository);

        libro = new Libro();
        libro.setId(1L);
        libro.setEstado(EstadoLibro.DISPONIBLE);
        libro.setTitulo("Test");
        libro.setAutor("Test");

    }

    @Test
    void registrarLibro() {

        //Configurar mocks
        when(libroRepository.save(any(Libro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //Ejecutar metodo
        Libro libro = libroServiceImpl.registrarLibro(new Libro());

        //Verificaciones
        assertNotNull(libro);
        assertEquals(EstadoLibro.DISPONIBLE,libro.getEstado());
    }

    @Test
    void obtenerTodosLosLibros() {

        when(libroRepository.findAll()).thenAnswer(invocation -> List.of(new Libro()));

        List<Libro> listLibros = libroServiceImpl.obtenerTodosLosLibros();

        assertNotNull(listLibros);
        assertFalse(listLibros.isEmpty());
    }

    @Test
    void eliminarLibro() {
        libroServiceImpl.eliminarLibro(1L);

        verify(libroRepository, times(1)).deleteById(1L);
    }

    @Test
    void actualizarLibro() {


        when(libroRepository.existsById(1L)).thenReturn(true);
        when(libroRepository.save(any(Libro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Libro result = libroServiceImpl.actualizarLibro(libro);
        assertNotNull(result);
        assertEquals(EstadoLibro.DISPONIBLE,result.getEstado());
        assertEquals("Test",result.getTitulo());
        assertEquals("Test",result.getAutor());
        verify(libroRepository, times(1)).existsById(1L);
        verify(libroRepository, times(1)).save(any(Libro.class));
    }

    @Test
    void actualizarLibroNotFound() {
        when(libroRepository.existsById(2L)).thenReturn(false);

        EntityNotFoundException result = assertThrows((EntityNotFoundException.class),
                () -> libroServiceImpl.actualizarLibro(libro));


    }


}