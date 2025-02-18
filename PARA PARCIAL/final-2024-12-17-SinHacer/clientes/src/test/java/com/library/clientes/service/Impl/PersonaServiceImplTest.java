package com.library.clientes.service.Impl;

import com.library.clientes.model.Persona;
import com.library.clientes.repository.PersonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PersonaServiceImplTest {

    public PersonaServiceImpl personaServiceImpl;

    @Mock
    private PersonaRepository personaRepository;

    Persona persona;
    Persona persona2;
    List<Persona> personas;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        personaServiceImpl = new PersonaServiceImpl(personaRepository);

        persona = new Persona();
        persona.setNombre("Jose");
        persona.setId(1L);
        persona.setDomicilio("Chacabuco");

        persona2 = new Persona();
        persona2.setNombre("Pepe");
        persona2.setId(2L);
        persona2.setDomicilio("Chacabuco");

        personas = new ArrayList<>();
        personas.add(persona);
        personas.add(persona2);

    }

    @Test
    void crearPersona() {
        when(personaRepository.save(any(Persona.class))).thenReturn(persona);

        Persona p = personaServiceImpl.crearPersona(persona);

        assertNotNull(p);
        assertEquals(persona.getId(), p.getId());
        assertEquals(persona.getNombre(), p.getNombre());
        assertEquals(persona.getDomicilio(), p.getDomicilio());
        verify(personaRepository, times(1)).save(any(Persona.class));
    }

    @Test
    void obtenerPersonaPorId() {
        when(personaRepository.findById(2L)).thenReturn(Optional.of(persona2));

        Optional<Persona> psn = personaServiceImpl.obtenerPersonaPorId(2L);
        assertTrue(psn.isPresent());
        assertEquals(persona2.getId(), psn.get().getId());
        assertEquals(persona2.getNombre(), psn.get().getNombre());
        assertEquals(persona2.getDomicilio(), psn.get().getDomicilio());
        verify(personaRepository, times(1)).findById(2L);
    }
    @Test
    void obtenerPersonaPorId_notFound() {
        when(personaRepository.findById(100L)).thenReturn(null);

        Optional<Persona> psn = personaServiceImpl.obtenerPersonaPorId(100L);
        assertNull(psn);
        verify(personaRepository, times(1)).findById(100L);
    }


    @Test
    void obtenerPersonaAlAzar() {
        when(personaRepository.findAll()).thenReturn(personas);

        Persona personaAleatoria = personaServiceImpl.obtenerPersonaAlAzar();

        assertNotNull(personaAleatoria);
        assertThat(personas).contains(personaAleatoria);
        verify(personaRepository, times(1)).findAll();
    }

    @Test
    void obtenerTodasLasPersonas() {
        when(personaRepository.findAll()).thenReturn(personas);

        List<Persona> allPersonas = personaServiceImpl.obtenerTodasLasPersonas();

        assertNotNull(allPersonas);
        assertEquals(personas.size(), allPersonas.size());
        verify(personaRepository, times(1)).findAll();
    }

    @Test
    void eliminarPersona() {
        personaServiceImpl.eliminarPersona(persona.getId());

        verify(personaRepository, times(1)).deleteById(persona.getId());
    }
}