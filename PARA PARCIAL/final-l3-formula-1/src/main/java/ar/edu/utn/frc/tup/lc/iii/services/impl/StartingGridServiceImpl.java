package ar.edu.utn.frc.tup.lc.iii.services.impl;

import ar.edu.utn.frc.tup.lc.iii.entities.StartingGridEntity;
import ar.edu.utn.frc.tup.lc.iii.models.*;
import ar.edu.utn.frc.tup.lc.iii.repositories.StartingGridRepository;
import ar.edu.utn.frc.tup.lc.iii.services.DriverService;
import ar.edu.utn.frc.tup.lc.iii.services.PhaseService;
import ar.edu.utn.frc.tup.lc.iii.services.StartingGridService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StartingGridServiceImpl implements StartingGridService {

    @Autowired
    private DriverService driverService;

    @Autowired
    private StartingGridRepository startingGridRepository;

    @Autowired
    private PhaseService phaseService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void createStartingGrid(Phase phase) {
        if(phase.getPhaseStage() == PhaseStage.Q1) {
            List<StartingGrid> startingGridList = new ArrayList<>();
            List<Driver> drivers = driverService.getAllDrivers();
            for (int i = 0; i < drivers.size(); i++) {
                StartingGrid startingGrid = new StartingGrid();
                startingGrid.setDriver(drivers.get(i));
                startingGrid.setPosition(i + 1);
                startingGrid.setPhase(phase);
                startingGridList.add(startingGrid);
            }
            startingGridRepository.saveAll(modelMapper.map(startingGridList, new TypeToken<List<StartingGridEntity>>() {}.getType()));


        }
    }
    @Override
    public void calculateNextStartingGrid(Phase phase) {
        // TODO 2 - Crear la lógica para poblar la grilla de largada de la siguiente fase.
        //   No se provee la lógica para este método, deberás implementarla de cero.
        //   Recordar el orden de las fases: Q1 -> Q2 -> Q3 -> R
        //      Es decir que si phase.getPhaseStage() es Q1, deberás poblar la grilla de largada de Q2.
        //   Recordar que las posiciónes de la fase de calculan automáticamente cada vez que se carga una nueva vuelta
        //   por lo que no es necesario calcularlas nuevamente, solo asegurarse de cargarlas en el orden correcto.
        //   IMPORTANTE: No olvidar guardar la grilla de largada en la base de datos.

        if(phase.getPhaseStage()==PhaseStage.Q3)
        {
            phase.setPhaseStage(PhaseStage.R);
            List<StartingGrid> mainRaceStartingGrid = new ArrayList<>();
            Race race = phase.getRace();

            Phase q1Phase = phaseService.getPhaseRace(race.getId(), PhaseStage.Q1);
            List<PhaseResult> q1PhaseResults = q1Phase.getPhaseResults();
            q1PhaseResults.sort(Comparator.comparing(PhaseResult::getPosition));
            Set<Integer> targetWorstPositions = new HashSet<>(Arrays.asList(16, 17, 18, 19, 20));

            List<PhaseResult> filteredQ1PhaseResults16to20 = q1PhaseResults.stream()
                            .filter(phaseResult -> targetWorstPositions.contains(phaseResult.getPosition()) )
                            .collect(Collectors.toList());

            Phase q2Phase = phaseService.getPhaseRace(race.getId(), PhaseStage.Q2);
            List<PhaseResult> q2PhaseResults = q2Phase.getPhaseResults();
            q2PhaseResults.sort(Comparator.comparing(PhaseResult::getPosition));
            Set<Integer> targetBadPositions = new HashSet<>(Arrays.asList(11, 12, 13, 14, 15));

            List<PhaseResult> filteredQ2PhaseResults11to15 = q2PhaseResults.stream()
                            .filter(phaseResult -> targetBadPositions.contains(phaseResult.getPosition()))
                            .collect(Collectors.toList());

            Phase q3Phase = phaseService.getPhaseRace(race.getId(), PhaseStage.Q3);
            List<PhaseResult> q3PhaseResults = q3Phase.getPhaseResults();
            q3PhaseResults.sort(Comparator.comparing(PhaseResult::getPosition));


            for (PhaseResult worstPhaseResult : filteredQ1PhaseResults16to20)
            {
                StartingGrid startingGridObject = new StartingGrid();
                startingGridObject.setPosition(worstPhaseResult.getPosition());
                startingGridObject.setPhase(phase);
                startingGridObject.setDriver(worstPhaseResult.getDriver());
                mainRaceStartingGrid.add(startingGridObject);
            }

            for ( PhaseResult badPhaseResult : filteredQ2PhaseResults11to15)
            {
                StartingGrid startingGridObject = new StartingGrid();
                startingGridObject.setPosition(badPhaseResult.getPosition());
                startingGridObject.setPhase(phase);
                startingGridObject.setDriver(badPhaseResult.getDriver());
                mainRaceStartingGrid.add(startingGridObject);
            }

            for(PhaseResult betterPhaseResult : q3PhaseResults)
            {

                StartingGrid startingGridObject = new StartingGrid();
                startingGridObject.setPosition(betterPhaseResult.getPosition());
                startingGridObject.setPhase(phase);
                startingGridObject.setDriver(betterPhaseResult.getDriver());
                mainRaceStartingGrid.add(startingGridObject);

            }
            startingGridRepository.saveAll(modelMapper.map(mainRaceStartingGrid, new TypeToken<List<StartingGridEntity>>() {}.getType() ));
        }
        else
        {

            //Reordenar los phaseResults en base a la performance del piloto en terminos de mejor tiempo
            List<PhaseResult> phaseResults = phase.getPhaseResults();
            phaseResults.sort(Comparator.comparing(PhaseResult::getPosition));
            List<StartingGrid> startingGridList = new ArrayList<>();
            int lastIndex = 0;

            switch (phase.getPhaseStage())
            {
                case Q1 -> {
                    lastIndex=15;
                    phase.setPhaseStage(PhaseStage.Q2);
                }
                case Q2 -> {
                    lastIndex=10;
                    phase.setPhaseStage(PhaseStage.Q3);
                }

            }
            for (int i = 0; i < lastIndex; i++)
            {
                StartingGrid startingGrid = new StartingGrid();
                startingGrid.setDriver(phaseResults.get(i).getDriver());
                startingGrid.setPhase(phase);
                startingGrid.setPosition(phaseResults.get(i).getPosition());
                startingGridList.add(startingGrid);
            }
            startingGridRepository.saveAll(modelMapper.map(startingGridList, new TypeToken<List<StartingGridEntity>>() {}.getType() ));
        }

    }
}
