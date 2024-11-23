package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.dtos.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EleccionesServiceImplTest {

    @InjectMocks
    EleccionesServiceImpl eleccionesService;
    @Mock
    RestClient restClient;

    DistritoDTO distritoDTO1;
    DistritoDTO distritoDTO2;
    DistritoDTO distritoDTO3;
    List<DistritoDTO> mockDistritos;

    CargoDTO cargoDTO;
    CargoDTO cargoDTO1;
    CargoDTO cargoDTO2;
    List<CargoDTO> mockCargos;

    SeccionDTO seccionDTO;
    SeccionDTO seccionDTO1;
    SeccionDTO seccionDTO2;
    List<SeccionDTO> mockSecciones;
    List<ResultadoEndpointDTO> mockResultados;
    AgrupacionDTO mockAgrupacionA;
    AgrupacionDTO mockAgrupacionB;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        distritoDTO1 = new DistritoDTO();
        distritoDTO1.setId(1L);
        distritoDTO1.setNombre("Pampa");

        distritoDTO2 = new DistritoDTO();
        distritoDTO2.setId(2L);
        distritoDTO2.setNombre("Jujuy");

        distritoDTO3 = new DistritoDTO();
        distritoDTO3.setId(3L);
        distritoDTO3.setNombre("Yrigoyen");

        mockDistritos = new ArrayList<>();
        mockDistritos.add(distritoDTO1);
        mockDistritos.add(distritoDTO2);
        mockDistritos.add(distritoDTO3);

        cargoDTO = new CargoDTO();
        cargoDTO.setId(1L);
        cargoDTO.setNombre("Pampa");

        cargoDTO1 = new CargoDTO();
        cargoDTO1.setId(2L);
        cargoDTO1.setNombre("Jujuy");

        cargoDTO2 = new CargoDTO();
        cargoDTO2.setId(3L);
        cargoDTO2.setNombre("Yrigoyen");

        mockCargos = new ArrayList<>();
        mockCargos.add(cargoDTO);
        mockCargos.add(cargoDTO1);
        mockCargos.add(cargoDTO2);

        seccionDTO = new SeccionDTO();
        seccionDTO.setId(1L);
        seccionDTO.setNombre("Pampa");

        seccionDTO1 = new SeccionDTO();
        seccionDTO1.setId(2L);
        seccionDTO1.setNombre("Jujuy");

        seccionDTO2 = new SeccionDTO();
        seccionDTO2.setId(3L);
        seccionDTO2.setNombre("Yrigoyen");

        mockSecciones = new ArrayList<>();
        mockSecciones.add(seccionDTO);
        mockSecciones.add(seccionDTO1);
        mockSecciones.add(seccionDTO2);

        // Setup Resultados
        mockResultados = new ArrayList<>();
        ResultadoEndpointDTO resultado1 = new ResultadoEndpointDTO();
        resultado1.setDistritoId(1L);
        resultado1.setVotosCantidad(1000);
        resultado1.setAgrupacionId(1L);

        ResultadoEndpointDTO resultado2 = new ResultadoEndpointDTO();
        resultado2.setDistritoId(1L);
        resultado2.setVotosCantidad(800);
        resultado2.setAgrupacionId(2L);

        mockResultados.add(resultado1);
        mockResultados.add(resultado2);

        // Setup Agrupaciones
        mockAgrupacionA = new AgrupacionDTO();
        mockAgrupacionA.setId(1L);
        mockAgrupacionA.setNombre("Partido A");

        mockAgrupacionB = new AgrupacionDTO();
        mockAgrupacionB.setId(2L);
        mockAgrupacionB.setNombre("Partido B");

    }

    @Test
    void getDistritosAllOrByName() {
        when(restClient.getAllDistritos(null)).thenReturn(mockDistritos);

        List<DistritoDTO> distritos = eleccionesService.getDistritosAllOrByName(null);

        verify(restClient, times(1)).getAllDistritos(null);

        assertEquals(1L, distritos.get(0).getId());
        assertEquals("Pampa", distritos.get(0).getNombre());

    }

    @Test
    void getDistritosAllOrByName_Null() {
        when(restClient.getAllDistritos("Wachiturro")).thenReturn(Collections.emptyList());

        List<DistritoDTO> distritos = eleccionesService.getDistritosAllOrByName("Wachiturro");

        verify(restClient, times(1)).getAllDistritos("Wachiturro");

        assertNull(distritos);
    }

    @Test
    void getDistritoById() {
        when(restClient.getDistritoById(1L)).thenReturn(distritoDTO1);

        DistritoDTO distrito = eleccionesService.getDistritoById(1L);

        verify(restClient, times(1)).getDistritoById(1L);

        assertEquals(1L, distrito.getId());
        assertEquals("Pampa", distrito.getNombre());
    }

    @Test
    void getDistritoById_Null() {
        when(restClient.getDistritoById(100L)).thenReturn(null);

        DistritoDTO distrito = eleccionesService.getDistritoById(100L);

        verify(restClient, times(1)).getDistritoById(100L);

        assertNull(distrito);
    }

    @Test
    void getCargosDistritoById() {
        when(restClient.getCargosById(1L)).thenReturn(mockCargos);

        List<CargoDTO> cargoDTOS = eleccionesService.getCargosDistrito(1L);

        verify(restClient, times(1)).getCargosById(1L);

        assertEquals(1L, cargoDTOS.get(0).getId());
        assertEquals("Pampa", cargoDTOS.get(0).getNombre());
    }

    @Test
    void getCargosDistritoById_Null() {
        when(restClient.getCargosById(777L)).thenReturn(Collections.emptyList());

        List<CargoDTO> cargoDTOS = eleccionesService.getCargosDistrito(777L);

        verify(restClient, times(1)).getCargosById(777L);

        assertNull(cargoDTOS);
    }

    @Test
    void getCargosByIdDistritoById() {
        when(restClient.getCargosByIdAndCargoId(1L, 1L)).thenReturn(cargoDTO);

        CargoDTO cargoDTO = eleccionesService.getCargoByIdAndDistritoId(1L, 1L);

        verify(restClient, times(1)).getCargosByIdAndCargoId(1L, 1L);

        assertEquals(1L, cargoDTO.getId());
        assertEquals("Pampa", cargoDTO.getNombre());
    }

    @Test
    void getCargosByIdDistritoById_Null() {
        when(restClient.getCargosByIdAndCargoId(77L, 1L)).thenReturn(null);

        CargoDTO cargoDTO = eleccionesService.getCargoByIdAndDistritoId(77L, 1L);

        verify(restClient, times(1)).getCargosByIdAndCargoId(77L, 1L);

        assertNull(cargoDTO);
    }


    @Test
    void getSeccionesDistritoById() {
        when(restClient.getSeccionById(1L)).thenReturn(mockSecciones);

        List<SeccionDTO> seccionDTOS = eleccionesService.getSeccionesByDistrito(1L);

        verify(restClient, times(1)).getSeccionById(1L);

        assertEquals(1L, seccionDTOS.get(0).getId());
        assertEquals("Pampa", seccionDTOS.get(0).getNombre());
    }

    @Test
    void getSeccionesDistritoById_Null() {
        when(restClient.getSeccionById(777L)).thenReturn(Collections.emptyList());

        List<SeccionDTO> seccionDTOS = eleccionesService.getSeccionesByDistrito(777L);

        verify(restClient, times(1)).getSeccionById(777L);

        assertNull(seccionDTOS);
    }

    @Test
    void getSeccionesByIdDistritoById() {
        when(restClient.getSeccionByIdAndDistritoId(1L, 1L)).thenReturn(seccionDTO);

        SeccionDTO seccion = eleccionesService.getSeccionByIdAndDistritoId(1L, 1L);

        verify(restClient, times(1)).getSeccionByIdAndDistritoId(1L, 1L);

        assertEquals(1L, seccion.getId());
        assertEquals("Pampa", seccion.getNombre());
    }

    @Test
    void getSeccionesByIdDistritoById_Null() {
        when(restClient.getSeccionByIdAndDistritoId(77L, 1L)).thenReturn(null);

        SeccionDTO seccion = eleccionesService.getSeccionByIdAndDistritoId(77L, 1L);

        verify(restClient, times(1)).getSeccionByIdAndDistritoId(77L, 1L);

        assertNull(seccion);
    }


    @Test
    void getResultadosByDistrito_WhenResultadosExist() {
        // Setup
        when(restClient.getResultadosByDistritoId(1L)).thenReturn(mockResultados);
        when(restClient.getDistritoById(1L)).thenReturn(distritoDTO1);
        when(restClient.getAllDistritos(null)).thenReturn(mockDistritos);
        when(restClient.getSeccionById(1L)).thenReturn(mockSecciones);
        when(restClient.getAgrupacionById(1L)).thenReturn(mockAgrupacionA);
        when(restClient.getAgrupacionById(2L)).thenReturn(mockAgrupacionB);

        // Execute
        ResultadoDistritoDTO resultado = eleccionesService.getResultadosByDistrito(1L);

        // Verify
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Pampa", resultado.getNombre());
        assertEquals(1800, resultado.getVotosEscrutados()); // 1000 + 800
        assertEquals(3, resultado.getSecciones().size());
        assertEquals("Partido A", resultado.getAgrupacionGanadora());

        // Verify resultados agrupaciones
        List<ResultadoAgrupacionDTO> resultadosAgrupaciones = resultado.getResultadosAgrupaciones();
        assertEquals(2, resultadosAgrupaciones.size());
        assertEquals("Partido A", resultadosAgrupaciones.get(0).getNombre());
        assertEquals(1, resultadosAgrupaciones.get(0).getPosicion());
        assertEquals(1000, resultadosAgrupaciones.get(0).getVotos());
    }

    @Test
    void getResultadosByDistrito_WhenNoResultados() {
        // Setup
        when(restClient.getResultadosByDistritoId(1L)).thenReturn(new ArrayList<>());

        // Execute
        ResultadoDistritoDTO resultado = eleccionesService.getResultadosByDistrito(1L);

        // Verify
        assertNull(resultado);
        verify(restClient, times(1)).getResultadosByDistritoId(1L);
    }

    @Test
    void mapToResultadoDistitoDTO() {
        // Setup
        when(restClient.getDistritoById(1L)).thenReturn(distritoDTO1);
        when(restClient.getAllDistritos(null)).thenReturn(mockDistritos);
        when(restClient.getSeccionById(1L)).thenReturn(mockSecciones);
        when(restClient.getAgrupacionById(1L)).thenReturn(mockAgrupacionA);
        when(restClient.getAgrupacionById(2L)).thenReturn(mockAgrupacionB);
        // Mock para calculatePadron
        when(restClient.getResultadosByDistritoId(1L)).thenReturn(Arrays.asList(
                createResultadoEndpointDTO(1L, 1000),
                createResultadoEndpointDTO(1L, 800)
        ));

        // Execute
        ResultadoDistritoDTO resultado = eleccionesService.mapToResultadoDistitoDTO(mockResultados);

        // Verify
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Pampa", resultado.getNombre());
        assertEquals(1800, resultado.getVotosEscrutados());
        assertEquals(3, resultado.getSecciones().size());
        assertEquals("Pampa", resultado.getSecciones().get(0));
        assertEquals("Jujuy", resultado.getSecciones().get(1));

        // Verificar porcentaje padrón
        assertNotNull(resultado.getPorcentajePadronNacional());

        // Verificar agrupación ganadora y resultados
        assertEquals("Partido A", resultado.getAgrupacionGanadora());
        List<ResultadoAgrupacionDTO> resultadosAgrupaciones = resultado.getResultadosAgrupaciones();
        assertEquals(2, resultadosAgrupaciones.size());
        assertEquals(1, resultadosAgrupaciones.get(0).getPosicion());
        assertEquals(2, resultadosAgrupaciones.get(1).getPosicion());
    }

    private ResultadoEndpointDTO createResultadoEndpointDTO(Long distritoId, Integer votosCantidad) {
        ResultadoEndpointDTO resultado = new ResultadoEndpointDTO();
        resultado.setDistritoId(distritoId);
        resultado.setVotosCantidad(votosCantidad);
        return resultado;
    }














}