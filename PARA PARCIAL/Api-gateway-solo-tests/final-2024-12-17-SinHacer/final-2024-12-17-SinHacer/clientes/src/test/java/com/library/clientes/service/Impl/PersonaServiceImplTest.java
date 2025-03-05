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
        MockitoAnnotations.openMocks(this);
        personaServiceImpl = new PersonaServiceImpl(personaRepository);

        persona = new Persona();
        persona.setNombre("Jose");
        persona.setDomicilio("Madrid");
        persona.setId(1L);

        persona2 = new Persona();
        persona2.setNombre("Pepe");
        persona2.setDomicilio("Juaca");
        persona2.setId(2L);

        personas = new ArrayList<>();
        personas.add(persona);
        personas.add(persona2);
    }

    @Test
    void crearPersona() {
        when(personaRepository.save(any(Persona.class))).thenReturn(persona);

        Persona p = personaServiceImpl.crearPersona(persona);

        assertNotNull(p);
        assertEquals(persona.getNombre(), p.getNombre());
        assertEquals(persona.getDomicilio(), p.getDomicilio());
        assertEquals(persona.getId(), p.getId());
        verify(personaRepository, times(1)).save(any(Persona.class));
    }

    @Test
    void obtenerPersonaPorId() {
        Long id = 1L;
        when(personaRepository.findById(id)).thenReturn(Optional.of(persona2));

        Optional<Persona> persona = personaServiceImpl.obtenerPersonaPorId(id);

        assertNotNull(persona);
        assertEquals(persona2.getNombre(), persona.get().getNombre());
        assertEquals(persona2.getDomicilio(), persona.get().getDomicilio());
        assertEquals(persona2.getId(), persona.get().getId());
        verify(personaRepository, times(1)).findById(id);
    }

    @Test
    void obtenerPersonaAlAzar() {
        when(personaRepository.findAll()).thenReturn(personas);

        Persona p = personaServiceImpl.obtenerPersonaAlAzar();

        assertNotNull(p);
        assertTrue(personas.contains(p));
        verify(personaRepository, times(1)).findAll();
    }

    @Test
    void obtenerTodasLasPersonas() {
        when(personaRepository.findAll()).thenReturn(personas);

        List<Persona> personasFinded = personaServiceImpl.obtenerTodasLasPersonas();

        assertNotNull(personas);
        assertEquals(personas.size(), personasFinded.size());
        verify(personaRepository, times(1)).findAll();
    }

    @Test
    void actualizarPersona() {
        Long id = 1L;
        when(personaRepository.findById(id)).thenReturn(Optional.of(persona2));
        when(personaRepository.save(any(Persona.class))).thenReturn(persona);

        Persona p = personaServiceImpl.actualizarPersona(id, persona2);

        assertNotNull(p);
        assertEquals(persona.getNombre(), p.getNombre());
        assertEquals(persona.getDomicilio(), p.getDomicilio());
        assertEquals(persona.getId(), p.getId());
        verify(personaRepository, times(1)).save(any(Persona.class));
        verify(personaRepository, times(1)).findById(id);

    }

    @Test
    void eliminarPersona() {
        Long id = 1L;

        personaServiceImpl.eliminarPersona(id);

        verify(personaRepository, times(1)).deleteById(id);
    }
}