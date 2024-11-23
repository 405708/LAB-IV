package ar.edu.utn.frc.tup.lc.iii.services.impl;

import ar.edu.utn.frc.tup.lc.iii.entities.DriverEntity;
import ar.edu.utn.frc.tup.lc.iii.entities.PhaseEntity;
import ar.edu.utn.frc.tup.lc.iii.entities.PhaseResultDetailEntity;
import ar.edu.utn.frc.tup.lc.iii.entities.PhaseResultEntity;
import ar.edu.utn.frc.tup.lc.iii.models.*;
import ar.edu.utn.frc.tup.lc.iii.repositories.PhaseResultDetailRepository;
import ar.edu.utn.frc.tup.lc.iii.repositories.PhaseResultRepository;
import ar.edu.utn.frc.tup.lc.iii.services.DriverService;
import ar.edu.utn.frc.tup.lc.iii.services.PhaseResultService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class PhaseResultServiceImpl implements PhaseResultService {

    @Autowired
    private DriverService driverService;

    @Autowired
    private PhaseResultRepository phaseResultRepository;

    @Autowired
    private PhaseResultDetailRepository phaseResultDetailRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PhaseResult recordLap(Phase phase, Long driverId, Duration time) {

        // TODO 1 - Registrar las vueltas de un piloto en una fase
        //  1. Buscar el piloto (Driver) con el id driverId usando el método provisto por driverService
        //  2. Buscar el resultado de la fase (PhaseResult) del piloto en la fase actual (phase) usando el método filterResultFromDriver
        //  3. Si el resultado de la fase no existe, crear un nuevo PhaseResult con el piloto, el tiempo,
        //     una lista vacía de detalles y la fase actual. Guardar el resultado de la fase en la base de datos y
        //  c.   agregarlo a la lista de resultados de la fase actual.
        //  4. Crear un nuevo PhaseResultDetail con el resultado de la fase (Phase), el piloto (Driver, el tiempo,
        //     el número de vuelta usando la cantidad de detalles del resultado de la fase + 1. Guardar el detalle
        //     de la vuelta en la base de datos.
        //  5. Agregar el detalle de la vuelta a la lista de detalles del resultado de la fase (PhaseResult).
        //  6. Actualizar el tiempo del resultado de la fase (PhaseResult) usando el método updatePhaseResultTime.
        //  7. Calcular las nuevas posiciones de los pilotos en la fase actual usando el método calculateNewPositions.
        //  8. Actualizar la posición del resultado de la fase (PhaseResult) del piloto usando el método filterResultFromDriver
        //     para obtener el resultado de la fase del piloto y tomar la posición nueva.
        //  9. Retornar el resultado de la fase (PhaseResult) del piloto.

        Driver driver = driverService.getDriverById(driverId);
        PhaseResult phaseResult = filterResultFromDriver(phase, driver.getId());

        if(phaseResult == null)
        {
            phaseResult = new PhaseResult();
            phaseResult.setDriver(driver);
            phaseResult.setTime(time);
            phaseResult.setPhase(phase);
            phaseResult.setDetails(new ArrayList<>());
            phaseResultRepository.save(modelMapper.map(phaseResult, PhaseResultEntity.class));
            phase.getPhaseResults().add(phaseResult); //3c. TODO Validar con un nuevo test que la lista phase.phaseResults no sea nula

        }

        PhaseResultDetail phaseResultDetail = new PhaseResultDetail();
        phaseResultDetail.setPhaseResult(phaseResult);
        phaseResultDetail.setDriver(driver);
        phaseResultDetail.setTime(time);
        phaseResultDetail.setLap(phaseResult.getDetails().size()+1); //5. TODO Validar con un nuevo test que la lista .details no sea nula y/o vacía
        phaseResultDetailRepository.save(modelMapper.map(phaseResultDetail, PhaseResultDetailEntity.class));
        phaseResult.getDetails().add(phaseResultDetail);
        updatePhaseResultTime(phaseResult, phase.getPhaseStage());
        calculateNewPositions(phase, phase.getPhaseStage());
        PhaseResult phaseResultWithUpdatedPosition = filterResultFromDriver(phase, driver.getId());
        phaseResult.setPosition(phaseResultWithUpdatedPosition.getPosition());
        phaseResult.setLaps(phaseResult.getDetails().size());
        return phaseResult;

    }

    private PhaseResult filterResultFromDriver(Phase phase, Long driverId) {
        return phase.getPhaseResults().stream()
                .filter(rd -> Objects.equals(rd.getDriver().getId(), driverId))
                .findFirst().orElse(null);
    }

    private void updatePhaseResultTime(PhaseResult phaseResult, PhaseStage phaseStage) {
        switch (phaseStage) {
            case Q1, Q2, Q3:
                phaseResult.getDetails().sort(Comparator.comparing(PhaseResultDetail::getTime));
                phaseResult.setTime(phaseResult.getDetails().get(0).getTime());
                break;
            case R:
                phaseResult.setTime(phaseResult.getDetails().stream().map(PhaseResultDetail::getTime).reduce(Duration.ZERO, Duration::plus));
                break;
        }
        phaseResultRepository.save(modelMapper.map(phaseResult, PhaseResultEntity.class));
    }

    private void calculateNewPositions(Phase phase, PhaseStage phaseStage) {
        switch (phaseStage) {
            case Q1, Q2, Q3:
                phase.getPhaseResults().sort(Comparator.comparing(PhaseResult::getTime));
                break;
            case R:
                phase.getPhaseResults().sort(Comparator.comparing(PhaseResult::getLaps).reversed().thenComparing(PhaseResult::getTime));
                break;
        }
        List<PhaseResult> phaseResults = phase.getPhaseResults();
        for (int i = 0; i < phaseResults.size(); i++) {
            phaseResults.get(i).setPosition(i + 1);
        }
        phaseResultRepository.saveAll(modelMapper.map(phaseResults, new org.modelmapper.TypeToken<List<PhaseResultEntity>>() {}.getType()));
    }
}
