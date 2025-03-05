package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.clients.VehicleRestClient;
import ar.edu.utn.frc.tup.lc.iv.entities.LotEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.LotPriceEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.LotTraceEntity;
import ar.edu.utn.frc.tup.lc.iv.models.*;
import ar.edu.utn.frc.tup.lc.iv.repositories.LotPriceRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.LotRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.LotTraceRepository;
import ar.edu.utn.frc.tup.lc.iv.services.LotService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LotServiceImpl implements LotService {


    private final LotRepository lotRepository;

    private final LotTraceRepository lotTraceRepository;

    private final LotPriceRepository lotPriceRepository;

    private final VehicleRestClient vehicleRestClient;

    private final ModelMapper modelMapper;

    public LotServiceImpl(LotRepository lotRepository, LotTraceRepository lotTraceRepository, LotPriceRepository lotPriceRepository, ModelMapper modelMapper, VehicleRestClient vehicleRestClient) {
        this.lotRepository = lotRepository;
        this.lotTraceRepository = lotTraceRepository;
        this.lotPriceRepository = lotPriceRepository;
        this.modelMapper = modelMapper;
        this.vehicleRestClient = vehicleRestClient;
    }

    @Override
    public List<Lot> findAll() {
        /*
         * NO CAMBIAR - NO HACE FALTA
         */
        List<LotEntity> lotEntities = lotRepository.findAll();
        List<Lot> lots = new ArrayList<>();
        for(LotEntity lotEntity : lotEntities) {
            Lot lot = mapLotWithVehicle(lotEntity);
            lots.add(lot);
        }
        return lots;
    }

    @Override
    public List<Lot> findAllByFloor(Floor floor) {
        /*
         * NO CAMBIAR - NO HACE FALTA
         */
        List<LotEntity> lotEntities = lotRepository.findAllByFloor(floor);
        List<Lot> lots = new ArrayList<>();
        for(LotEntity lotEntity : lotEntities) {
            Lot lot = mapLotWithVehicle(lotEntity);
            lots.add(lot);
        }
        return lots;
    }

    @Override
    public Lot findById(Long id) {
        /*
         * NO CAMBIAR - NO HACE FALTA
         */
        LotEntity lotEntity = lotRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lot id: " + id + " not found."));
        return mapLotWithVehicle(lotEntity);
    }

    @Override
    public LotTrace saleLot(Long lotId, Vehicle vehicle, LocalDateTime entryDateTime) {
        /*
         * NO CAMBIAR - NO HACE FALTA
         */
        Lot lot = this.findById(lotId);
        if(!lot.getSection().name().equals(vehicle.getType())) {
            throw new IllegalArgumentException("Vehicle type does not match lot section.");
        }
        if(lot.getType() != LotType.PRIVADO) {
            throw new IllegalArgumentException("Lot id " + lotId + " is not for sale.");
        }
        if(lot.getOccupiedBy() == null && lot.getStatus().equals(LotStatus.LIBRE)) {
            LotTrace lotTrace = new LotTrace();
            lotTrace.setVehicle(vehicle);
            lotTrace.setEntryDateTime(entryDateTime);
            LotPriceEntity lotPriceEntity = lotPriceRepository.findAllByTypeAndSectionAndActiveTrue(lot.getType(), lot.getSection());
            lotTrace.setLotPrice(modelMapper.map(lotPriceEntity, LotPrice.class));
            lotTrace.setAmount(lotPriceEntity.getPrice());
            LotTraceEntity lotTraceEntity = modelMapper.map(lotTrace, LotTraceEntity.class);
            lotTraceEntity = lotTraceRepository.save(lotTraceEntity);
            lot.setOccupiedBy(modelMapper.map(lotTraceEntity, LotTrace.class));
            lot.setStatus(LotStatus.OCUPADO);
        } else {
            throw new IllegalArgumentException("Lot id " + lotId + " is already occupied.");
        }
        LotEntity lotEntity = lotRepository.save(modelMapper.map(lot, LotEntity.class));
        return modelMapper.map(lotEntity.getOccupiedBy(), LotTrace.class);
    }

    @Override
    public LotTrace rentLot(Long lotId, Vehicle vehicle, Integer months, LocalDateTime entryDateTime) {
        // TODO: Implementar
        /*
            Buscar el lot por el id, si la sección no se corresponde con el tipo de vehículo lanzar una
            excepción
            de tipo IllegalArgumentException con el mensaje "Vehicle type does not match lot section."
             validar que
            el tipo de lot sea ALQUILER_MENSUAL, si no lo es lanzar una excepción de tipo
            IllegalArgumentException con el mensaje "Lot id {lotId} is not for rent.".

            Si el lot ya está ocupado lanzar una excepción
            de tipo IllegalArgumentException con el mensaje "Lot id {lotId} is already occupied.".


            Si pasa las validaciones anteriores, crear un objeto LotTrace y setearle sus valores y
            calcular el monto a pagar multiplicando el precio del lot por la cantidad de meses.
            Actualizar estado del lot, guardar y retornar el objeto.
         */
        Optional<LotEntity> lot = lotRepository.findById(lotId);
        LotEntity lotEntity;
        if(lot.isPresent()) {
            lotEntity = lot.get();
        }
        else {
            return null;
        }

        if(!lotEntity.getSection().name().equals(vehicle.getType())) {
            throw new IllegalArgumentException("Vehicle type does not match lot section.");
        }
        if(lotEntity.getType() != LotType.ALQUILER_MENSUAL) {
            throw new IllegalArgumentException("Lot id " + lotId + " is not for rent.");
        }
        if(lotEntity.getStatus().equals(LotStatus.OCUPADO)) {
            throw new IllegalArgumentException("Lot id " + lotId + " is already occupied.");
        }

        LotTrace lotTrace = new LotTrace();
        lotTrace.setVehicle(vehicle);
        lotTrace.setEntryDateTime(entryDateTime);
        LotPriceEntity lotPrice = lotPriceRepository.findAllByTypeAndSectionAndActiveTrue(lotEntity.getType(), lotEntity.getSection());
        lotTrace.setLotPrice(modelMapper.map(lotPrice, LotPrice.class));
        BigDecimal price = lotPrice.getPrice().multiply(new BigDecimal(months));
        lotTrace.setAmount(price);

        lotEntity.setStatus(LotStatus.OCUPADO);
        lotEntity.setOccupiedBy(modelMapper.map(lotTrace, LotTraceEntity.class));
        lotRepository.save(lotEntity);

        return lotTrace;
    }

    @Override
    public LotTrace entryLot(Long lotId, Vehicle vehicle, LocalDateTime entryDateTime) {
        // TODO: Implementar
        /*
            Buscar el lot por el id, si la sección no se corresponde con el tipo de vehículo lanzar una excepción
            de tipo IllegalArgumentException con el mensaje "Vehicle type does not match lot section."
            */
        Optional<LotEntity> lot = lotRepository.findById(lotId);
        LotEntity lotEntity;
        if(lot.isPresent()) {
            lotEntity = lot.get();
        }
        else {
            return null;
        }
        if(!lotEntity.getSection().name().equals(vehicle.getType())) {
            throw new IllegalArgumentException("Vehicle type does not match lot section.");
        }
        /*
            Luego validar que el tipo de lot sea TEMPORARIO, si no lo es lanzar una excepción de tipo
             IllegalArgumentException con el mensaje "Lot id {lotId} is not for temporary use.".
                */
        if(lotEntity.getType() != LotType.TEMPORARIO) {
            throw new IllegalArgumentException("Lot id " + lotId + " is not for temporary use.");
        }
        /*
             Si el lot ya está ocupado lanzar una excepción
            de tipo IllegalArgumentException con el mensaje "Lot id {lotId} is already occupied.".
                */
        if(lotEntity.getStatus().equals(LotStatus.OCUPADO)) {
            throw new IllegalArgumentException("Lot id " + lotId + " is already occupied.");
        }
        /*
            Si pasa las validaciones anteriores, crear un objeto LotTrace y setearle sus valores
             pero no calcular precio ya que se desconoce la fecha de salida.
             Actualizar estado del lot, guardar y retornar el objeto.
         */
        LotTrace lotTrace = new LotTrace();
        lotTrace.setVehicle(vehicle);
        lotTrace.setEntryDateTime(entryDateTime);
        LotPriceEntity lotPrice = lotPriceRepository.findAllByTypeAndSectionAndActiveTrue(lotEntity.getType(), lotEntity.getSection());
        lotTrace.setLotPrice(modelMapper.map(lotPrice, LotPrice.class));

        lotEntity.setStatus(LotStatus.OCUPADO);
        lotEntity.setOccupiedBy(modelMapper.map(lotTrace, LotTraceEntity.class));
        lotRepository.save(lotEntity);

        return lotTrace;
    }

    @Override
    public LotTrace exitTrace(Long lotId, LocalDateTime exitDateTime) {
        // TODO: Implementar
        /*
            Buscar el lot por el id,
            */
        Optional<LotEntity> lot = lotRepository.findById(lotId);
        LotEntity lotEntity;
        if(lot.isPresent()) {
            lotEntity = lot.get();
        }
        else {
            return null;
        }
            /*
            si el lot no está ocupado lanzar una excepción
            de tipo EntityNotFoundException con el mensaje "Lot id {lotId} is not occupied.".
            */
        if(lotEntity.getStatus().equals(LotStatus.LIBRE)) {
            throw new EntityNotFoundException("Lot id " + lotId + " is not occupied.");
        }
            /*
            Validar que la fecha de salida
            no sea posterior a la fecha de entrada, si lo es lanzar una excepción de tipo IllegalArgumentException con el mensaje
            "Exit date time cannot be before entry date time.".
            */
        if(exitDateTime.isBefore(lotEntity.getOccupiedBy().getEntryDateTime())){
            throw new IllegalArgumentException("Exit date time cannot be before entry date time.");
        }

            /*
            Si pasa las validaciones anteriores, actualizar la fecha de salida y el estado del lot a LIBRE, calcular el monto
            a pagar multiplicando el precio del lot por la cantidad de horas que estuvo ocupado y guardar el objeto LotTrace,
            guardar el objeto Lot y retornar el objeto LotTrace.
         */
        lotEntity.setStatus(LotStatus.LIBRE);
        lotEntity.getOccupiedBy().setExitDateTime(exitDateTime);
        lotRepository.save(lotEntity);

        Lot lotVehicule = mapLotWithVehicle(lotEntity);

        Long hoursStayed = Duration.between(lotVehicule.getOccupiedBy().getEntryDateTime(), exitDateTime).toHours();
        System.out.println("HORAS STAYADAS "+hoursStayed);
        BigDecimal total =  lotVehicule.getOccupiedBy().getLotPrice().getPrice().multiply(BigDecimal.valueOf(hoursStayed));

        LotTrace lotTrace = new LotTrace();
        lotTrace.setAmount(total);
        lotTrace.setEntryDateTime(lotEntity.getOccupiedBy().getEntryDateTime());
        lotTrace.setExitDateTime(exitDateTime);

        lotTrace.setVehicle(lotVehicule.getOccupiedBy().getVehicle());

        return lotTrace;
    }

    public Lot mapLotWithVehicle(LotEntity lotEntity) {
        /*
         * NO CAMBIAR - NO HACE FALTA
         */
        Lot lot = modelMapper.map(lotEntity, Lot.class);
        if(lotEntity.getOccupiedBy() != null) {
            LotTrace lotTrace = new LotTrace();
            lotTrace.setId(lotEntity.getOccupiedBy().getId());
            lotTrace.setEntryDateTime(lotEntity.getOccupiedBy().getEntryDateTime());
            lotTrace.setExitDateTime(lotEntity.getOccupiedBy().getExitDateTime());
            lotTrace.setAmount(lotEntity.getOccupiedBy().getAmount());
            lotTrace.setLotPrice(modelMapper.map(lotEntity.getOccupiedBy().getLotPrice(), LotPrice.class));
            ResponseEntity<Vehicle> vehicle = vehicleRestClient.getVehicleById(lotEntity.getOccupiedBy().getVehicleId());
            lotTrace.setVehicle(vehicle.getBody());
            lot.setOccupiedBy(lotTrace);
        }
        return lot;
    }
}
