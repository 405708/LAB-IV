package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.clients.VehicleRestClient;
import ar.edu.utn.frc.tup.lc.iv.entities.LotEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.LotPriceEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.LotTraceEntity;
import ar.edu.utn.frc.tup.lc.iv.models.*;
import ar.edu.utn.frc.tup.lc.iv.repositories.LotPriceRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.LotRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.LotTraceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LotServiceImplTest {

    public LotServiceImpl lotService;

    @Mock
    private LotRepository lotRepository;

    @Mock
    private LotPriceRepository lotPriceRepository;

    @Mock
    private LotTraceRepository lotTraceRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private VehicleRestClient vehicleRestClient;
    Lot lot;
    List<Lot> lots;
    LotEntity lotEntity;
    LotEntity lotEntityWithoutOccupant;
    List<LotEntity> entitiesWithoutOccupant;
    List<LotEntity> lotsEntities;
    LotPriceEntity lotPriceEntity;
    LotTraceEntity lotTraceEntity;
    Vehicle vehicle;
    LotPrice lotPrice;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        lotService = new LotServiceImpl(lotRepository, lotTraceRepository, lotPriceRepository, modelMapper, vehicleRestClient);
        lot = new Lot();
        lot.setFloor(Floor.PLANTA_BAJA);
        lot.setType(LotType.ALQUILER_MENSUAL);
        lot.setStatus(LotStatus.LIBRE);
        lot.setSection(Section.MOTOS);
        lots = new ArrayList<>();
        lots.add(lot);

        lotEntity = new LotEntity();
        lotEntity.setFloor(Floor.PLANTA_BAJA);
        lotEntity.setType(LotType.ALQUILER_MENSUAL);
        lotEntity.setStatus(LotStatus.LIBRE);
        lotEntity.setSection(Section.MOTOS);
        lotEntity.setOccupiedBy(null);

        lotsEntities = new ArrayList<>();
        lotsEntities.add(lotEntity);

        lotPriceEntity = new LotPriceEntity();
        lotPriceEntity.setType(LotType.ALQUILER_MENSUAL);
        lotPriceEntity.setSection(Section.MOTOS);
        lotPriceEntity.setPrice(BigDecimal.valueOf(10));
        lotPriceEntity.setValidFrom(LocalDateTime.now());
        lotPriceEntity.setValidTo(LocalDateTime.now());

        lotPrice = new LotPrice();
        lotPrice.setType(LotType.ALQUILER_MENSUAL);
        lotPrice.setSection(Section.MOTOS);
        lotPrice.setPrice(BigDecimal.valueOf(10));
        lotPrice.setValidFrom(LocalDateTime.now());
        lotPrice.setValidTo(LocalDateTime.now());

        lotTraceEntity = new LotTraceEntity();
        lotTraceEntity.setLotPrice(lotPriceEntity);
        lotTraceEntity.setAmount(BigDecimal.valueOf(30));
        lotTraceEntity.setVehicleId("AA123AA");
        lotEntity.setOccupiedBy(lotTraceEntity);

        vehicle = new Vehicle();
        vehicle.setId("AA123AA");
        vehicle.setType("MOTOS");

        lotEntityWithoutOccupant = new LotEntity();
        lotEntityWithoutOccupant.setFloor(Floor.PLANTA_BAJA);
        lotEntityWithoutOccupant.setType(LotType.ALQUILER_MENSUAL);
        lotEntityWithoutOccupant.setStatus(LotStatus.LIBRE);
        lotEntityWithoutOccupant.setSection(Section.MOTOS);
        lotEntityWithoutOccupant.setOccupiedBy(null);

        entitiesWithoutOccupant = new ArrayList<>();
        entitiesWithoutOccupant.add(lotEntityWithoutOccupant);

    }

    @Test
    void findAll_shouldReturnAllLots() {
        // Arrange
        when(lotRepository.findAll()).thenReturn(entitiesWithoutOccupant);
        when(modelMapper.map(any(LotEntity.class), eq(Lot.class))).thenReturn(lot);

        // Act
        List<Lot> result = lotService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        // Verify
        verify(lotRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(any(LotEntity.class), eq(Lot.class));
    }

    @Test
    void findAllByFloor_shouldReturnLotsByFloor() {
        // Arrange
        Floor floor = Floor.PLANTA_BAJA;

        when(lotRepository.findAllByFloor(floor)).thenReturn(entitiesWithoutOccupant);
        when(modelMapper.map(any(LotEntity.class), eq(Lot.class))).thenReturn(lot);

        // Act
        List<Lot> result = lotService.findAllByFloor(floor);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        // Verify
        verify(lotRepository, times(1)).findAllByFloor(floor);
        verify(modelMapper, times(1)).map(any(LotEntity.class), eq(Lot.class));
    }

    @Test
    void findById() {
        Long id = 1L;

        when(lotRepository.findById(id)).thenReturn(Optional.of(lotEntityWithoutOccupant));
        when(modelMapper.map(any(LotEntity.class), eq(Lot.class))).thenReturn(lot);

        Lot found = lotService.findById(id);

        assertNotNull(found);

        verify(lotRepository, times(1)).findById(id);
        verify(modelMapper, times(1)).map(any(LotEntity.class), eq(Lot.class));
    }

    @Test
    void saleLot() {
        Lot lotSale = new Lot();
        lotSale.setFloor(Floor.PLANTA_BAJA);
        lotSale.setType(LotType.PRIVADO);
        lotSale.setStatus(LotStatus.LIBRE);
        lotSale.setSection(Section.MOTOS);
        lotSale.setOccupiedBy(null);

        Long id = 1L;

        when(lotRepository.findById(id)).thenReturn(Optional.of(lotEntityWithoutOccupant));
        when(modelMapper.map(any(LotEntity.class), eq(Lot.class))).thenReturn(lotSale);
        when(lotPriceRepository.findAllByTypeAndSectionAndActiveTrue(LotType.PRIVADO,Section.MOTOS)).thenReturn(lotPriceEntity);
        when(lotTraceRepository.save(any())).thenReturn(lotTraceEntity);
        when(lotRepository.save(any())).thenReturn(lotEntity);

        LotTrace lotTrace = lotService.saleLot(id,vehicle,LocalDateTime.now());

//        assertNotNull(lotTrace);
        assertEquals(modelMapper.map(lotTraceEntity, LotTrace.class), lotTrace);

    }

    @Test
    void rentLot() {
        Long id = 1L;

        when(lotRepository.findById(id)).thenReturn(Optional.of(lotEntityWithoutOccupant));
        when(modelMapper.map(any(LotEntity.class), eq(Lot.class))).thenReturn(lot);
        when(lotPriceRepository.findAllByTypeAndSectionAndActiveTrue(LotType.ALQUILER_MENSUAL,Section.MOTOS)).thenReturn(lotPriceEntity);
        when(modelMapper.map(any(LotPriceEntity.class), eq(LotPrice.class))).thenReturn(lotPrice);

        LotTrace found = lotService.rentLot(id, vehicle, 3, LocalDateTime.now());

        assertNotNull(found);

        assertEquals(BigDecimal.valueOf(30), found.getAmount());

        verify(lotRepository, times(1)).findById(id);

    }


    
    @Test
    void entryLot() {
    }

    @Test
    void exitTrace() {
    }
}