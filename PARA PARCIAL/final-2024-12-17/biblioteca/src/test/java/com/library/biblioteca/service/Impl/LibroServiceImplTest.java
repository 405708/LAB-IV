package com.library.biblioteca.service.Impl;

import com.library.biblioteca.RandomDataForObject;
import com.library.biblioteca.enums.EstadoLibro;
import com.library.biblioteca.model.Libro;
import com.library.biblioteca.repository.LibroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LibroServiceImplTest {

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private LibroServiceImpl sut;

    private Libro libro;

    private List<Libro> libros;

    @BeforeEach
    void setUp() throws IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        libros = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            libros.add(RandomDataForObject.generateRandomValuesCascade(new Libro()));
        }

        libro = RandomDataForObject.generateRandomValuesCascade(new Libro());
    }

    @Test
    void registrarLibroTest() {
        libro.setEstado(EstadoLibro.DISPONIBLE);
        when(libroRepository.save(any())).thenReturn(libro);

        Libro result = sut.registrarLibro(libro);

        assertEquals(EstadoLibro.DISPONIBLE, result.getEstado());
        verify(libroRepository, times(1)).save(any());
        assertEquals(libro.getId(), result.getId());
        assertEquals(libro.getAutor(), result.getAutor());
        assertEquals(libro.getTitulo(), result.getTitulo());
        assertEquals(libro.getTitulo(), result.getTitulo());
    }

    @Test
    void obtenerTodosLosLibrosTest() {
        when(libroRepository.findAll()).thenReturn(libros);

        List<Libro> result = sut.obtenerTodosLosLibros();

        assertEquals(3, result.size());
        verify(libroRepository, times(1)).findAll();
    }

    @Test
    void eliminarLibroTest() {
        sut.eliminarLibro(1L);

        verify(libroRepository, times(1)).deleteById(1L);
    }

    @Test
    void actualizarLibroTestSuccess() {
        libro.setId(1L);
        when(libroRepository.existsById(libro.getId())).thenReturn(true);
        when(libroRepository.save(any())).thenReturn(libro);

        Libro result = sut.actualizarLibro(libro);

        verify(libroRepository, times(1)).save(any());
        assertEquals(libro.getId(), result.getId());
        assertEquals(libro.getEstado(), result.getEstado());
        assertEquals(libro.getAutor(), result.getAutor());
        assertEquals(libro.getTitulo(), result.getTitulo());
        assertEquals(libro.getTitulo(), result.getTitulo());
    }

    @Test
    void actualizarLibroTestNotFound() {
        libro.setId(1L);
        when(libroRepository.existsById(libro.getId())).thenReturn(false);
        when(libroRepository.save(any())).thenReturn(libro);

        EntityNotFoundException result = assertThrows((EntityNotFoundException.class),
                () -> sut.actualizarLibro(libro));
    }

}
