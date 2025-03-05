package com.library.biblioteca.service.Impl;

import com.library.biblioteca.enums.EstadoLibro;
import com.library.biblioteca.model.Libro;
import com.library.biblioteca.repository.LibroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LibroServiceImplTest {

    public LibroServiceImpl libroServiceImpl;

    @Mock
    public LibroRepository libroRepository;

    Libro libro;
    Libro libro2;

    List<Libro> libros;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        libroServiceImpl = new LibroServiceImpl(libroRepository);

        libro = new Libro();
        libro.setTitulo("Libro 1");
        libro.setAutor("Libro Autor");
        libro.setEstado(EstadoLibro.DISPONIBLE);
        libro.setIsbn("1234567890");
        libro.setId(1L);

        libro2 = new Libro();
        libro2.setTitulo("Libro 2");
        libro2.setAutor("Libro Autor");
        libro2.setEstado(EstadoLibro.RESERVADO);
        libro2.setIsbn("987654321");
        libro.setId(2L);

        libros = new ArrayList<>();
        libros.add(libro);
        libros.add(libro2);

    }

    @Test
    void registrarLibro() {
        when(libroRepository.save(any(Libro.class))).thenReturn(libro);

        Libro libroSaved = libroServiceImpl.registrarLibro(libro);

        assertNotNull(libroSaved);
        assertEquals(libro.getTitulo(), libroSaved.getTitulo());
        assertEquals(libro.getAutor(), libroSaved.getAutor());
        assertEquals(libro.getIsbn(), libroSaved.getIsbn());
        assertEquals(libro.getId(), libroSaved.getId());
        assertEquals(libro.getEstado(), libroSaved.getEstado());
        verify(libroRepository, times(1)).save(any(Libro.class));

    }

    @Test
    void obtenerTodosLosLibros() {
        when(libroRepository.findAll()).thenReturn(libros);

        List<Libro> libroList = libroServiceImpl.obtenerTodosLosLibros();

        assertNotNull(libroList);
        assertEquals(libros.size(), libroList.size());
        verify(libroRepository, times(1)).findAll();

    }

    @Test
    void eliminarLibro() {
        Long id = libro.getId();

        libroServiceImpl.eliminarLibro(id);

        verify(libroRepository, times(1)).deleteById(id);
    }

    @Test
    void actualizarLibro() {
        Long id = libro.getId();
        when(libroRepository.existsById(id)).thenReturn(true);
        when(libroRepository.save(any(Libro.class))).thenReturn(libro);

        Libro libroSaved = libroServiceImpl.actualizarLibro(libro);

        assertNotNull(libroSaved);
        assertEquals(libro.getTitulo(), libroSaved.getTitulo());
        assertEquals(libro.getAutor(), libroSaved.getAutor());
        assertEquals(libro.getIsbn(), libroSaved.getIsbn());
        assertEquals(libro.getId(), libroSaved.getId());
        assertEquals(libro.getEstado(), libroSaved.getEstado());

    }

    @Test
    void actualizarLibro_NoExiste() {
        Long id = 999L;
        when(libroRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> libroServiceImpl.actualizarLibro(libro));

    }
}