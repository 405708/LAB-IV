package com.library.clientes.service.Impl;

import com.library.clientes.RandomDataForObject;
import com.library.clientes.model.Persona;
import com.library.clientes.repository.PersonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PersonaServiceImplTest {

    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private PersonaServiceImpl sut;

    private Persona persona;

    private List<Persona> personas;

    @BeforeEach
    void setUp() throws IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        personas = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            personas.add(RandomDataForObject.generateRandomValue(new Persona()));
        }

        persona = RandomDataForObject.generateRandomValue(new Persona());
    }

    @Test
    void crearPersonaTest() {
        when(personaRepository.save(any())).thenReturn(persona);

        Persona result = sut.crearPersona(persona);

        verify(personaRepository, times(1)).save(any());
        assertEquals(persona.getId(), result.getId());
        assertEquals(persona.getNombre(), result.getNombre());
        assertEquals(persona.getDomicilio(), result.getDomicilio());
    }

    @Test
    void obtenerPersonaPorIdTest() {

        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));

        Optional<Persona> result = sut.obtenerPersonaPorId(1L);

        assertFalse(result.isEmpty());
        assertEquals(persona.getId(), result.get().getId());
        assertEquals(persona.getNombre(), result.get().getNombre());
        assertEquals(persona.getDomicilio(), result.get().getDomicilio());
    }

    @Test
    void obtenerPersonaAlAzarTest() {
        when(personaRepository.findAll()).thenReturn(personas);

        Persona result = sut.obtenerPersonaAlAzar();

        assertNotNull(result);
    }

    @Test
    void obtenerTodasLasPersonasTest() {
        when(personaRepository.findAll()).thenReturn(personas);

        List<Persona> result = sut.obtenerTodasLasPersonas();

        assertEquals(3, result.size());
    }

    @Test
    void actualizarPersonaTest() {
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));
        when(personaRepository.save(any())).thenReturn(persona);

        Persona result = sut.actualizarPersona(1L, persona);

        verify(personaRepository, times(1)).save(any());
        assertEquals(persona.getId(), result.getId());
        assertEquals(persona.getNombre(), result.getNombre());
        assertEquals(persona.getDomicilio(), result.getDomicilio());
    }

    @Test
    void eliminarPersonaTest() {
        sut.eliminarPersona(1L);

        verify(personaRepository, times(1)).deleteById(1L);
    }
}
