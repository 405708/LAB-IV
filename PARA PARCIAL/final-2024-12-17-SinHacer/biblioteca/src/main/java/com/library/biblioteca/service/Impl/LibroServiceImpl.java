package com.library.biblioteca.service.Impl;

import com.library.biblioteca.enums.EstadoLibro;
import com.library.biblioteca.model.Libro;
import com.library.biblioteca.repository.LibroRepository;
import com.library.biblioteca.service.LibroService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;

    public LibroServiceImpl(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Override
    public Libro registrarLibro(Libro libro) {
        //TOD
        /**
         * Completar el metodo de registro
         * el estado inicial del libro debe ser DISPONIBLE
         */
        libro.setEstado(EstadoLibro.DISPONIBLE);

        return libroRepository.save(libro);
    }

    @Override
    public List<Libro> obtenerTodosLosLibros() {
        //TOD
        /**
         * Completar el metodo 
         */

        return libroRepository.findAll();
    }

    @Override
    public void eliminarLibro(Long id) {
        //TOD
        /**
         * Completar el metodo
         */
        libroRepository.deleteById(id);
    }

    @Override
    public Libro actualizarLibro(Libro libro) {
        //TOD
        /**
         * Completar el metodo
         */
        if (libroRepository.existsById(libro.getId())) {
            return libroRepository.save(libro);
        } else {
            throw new EntityNotFoundException();
        }
    }
}
