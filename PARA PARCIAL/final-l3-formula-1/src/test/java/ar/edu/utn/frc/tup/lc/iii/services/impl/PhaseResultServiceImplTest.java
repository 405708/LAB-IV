package ar.edu.utn.frc.tup.lc.iii.services.impl;
import ar.edu.utn.frc.tup.lc.iii.entities.PhaseResultDetailEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.platform.commons.support.ReflectionSupport;

import ar.edu.utn.frc.tup.lc.iii.entities.PhaseResultEntity;
import ar.edu.utn.frc.tup.lc.iii.models.*;
import ar.edu.utn.frc.tup.lc.iii.repositories.PhaseResultDetailRepository;
import ar.edu.utn.frc.tup.lc.iii.repositories.PhaseResultRepository;
import ar.edu.utn.frc.tup.lc.iii.services.DriverService;
import ar.edu.utn.frc.tup.lc.iii.services.PhaseResultService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.support.ReflectionSupport;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.platform.commons.support.ReflectionSupport.findMethod;
import static org.junit.platform.commons.support.ReflectionSupport.invokeMethod;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PhaseResultServiceImplTest {


    @MockBean
    PhaseResultDetailRepository phaseResultDetailRepository;
    @MockBean
    PhaseResultRepository phaseResultRepository;

    @MockBean
    DriverService driverService;


    @SpyBean
    PhaseResultService phaseResultService;

    @SpyBean
    PhaseResultServiceImpl phaseResultServiceImpl;


    @Test

   //TODO: Identificar cual sería el contexto común a todos los tests y ver qué se puede precargar con alguna de las anotaciones de abajo
    //@BeforeAll
    //@BeforeEach

    void recordLapTestPhaseNotNull() {
        /*

    #Caso 1:
   -Una phase de Q3 recibe una lap de un phaseResult existente encontrado por filterResultFromDriver
    -Tiene 2 details existentes y va a recibir uno nuevo con aún mejor tiempo
    -Testear que el phaseResult saliente tenga como mejor tiempo el del detail (lap) anterior
    -Testear que el phaseResult y sus details tienen relacion M-D

         */
        //Given
        Race race = new Race(1L, "Mock Race", LocalDate.now(), "Montecarlo");
        Phase phase = new Phase();
        phase.setRace(race);
        phase.setPhaseStage(PhaseStage.Q3);
        Driver driver = new Driver(12L, "Driver", "Winner", 1, Team.Mercedes);

        PhaseResult newPhaseResult = new PhaseResult();
        newPhaseResult.setPhase(phase);
        newPhaseResult.setDriver(driver);

        List<PhaseResultDetail> phaseResultDetails = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            PhaseResultDetail detail = new PhaseResultDetail();
            detail.setId((long) i+1);
            detail.setPhaseResult(newPhaseResult);
            detail.setDriver(driver);
            detail.setLap(i+1);
            detail.setTime(Duration.ofSeconds(60-i));
            phaseResultDetails.add(detail);
        } // El mejor tiempo de estos dos details (laps) va a ser 59. El parametro time va a ser 55

        newPhaseResult.setDetails(phaseResultDetails);
        List<PhaseResult> phaseResults = new ArrayList<>();
        phaseResults.add(newPhaseResult);
        phase.setPhaseResults(phaseResults); //TODO Validar el funcionamiento de este otro camino

      /*  phase.getPhaseResults().add(newPhaseResult); */

        //When
        when(driverService.getDriverById(12L)).thenReturn(driver);
        when(phaseResultRepository.save(any(PhaseResultEntity.class))).thenReturn(new PhaseResultEntity());
        when(phaseResultDetailRepository.save(any(PhaseResultDetailEntity.class))).thenReturn(new PhaseResultDetailEntity());

        //y sin mockear el repository ?

        //Then

        PhaseResult resultPhaseResult = phaseResultService.recordLap(phase, 12L, Duration.ofSeconds(55) );
        assertEquals(3, resultPhaseResult.getLaps()); //Si tenía dos detalles, con el nuevo recordLap tiene que terminar teniendo 3 laps
        assertEquals(Duration.ofSeconds(55), resultPhaseResult.getTime());
        assertAll("Relacion M-D",
            resultPhaseResult.getDetails().stream()
                    .map(detail -> () -> assertEquals(resultPhaseResult.getId(), detail.getPhaseResult().getId()))

        );


    }
    @Test
    void recordLapTestPhaseNull()
    {
        //Validar:
        //t1. Que el phaseResult salga nulo del metodo privado filterResultFromDriver
        //Si el PhaseResult de salida tiene el mismo driver y time que los dados por parámetro, esto sería indicativo de lo anterior
        //t2. La nulidad o no de la lista phaseResults de la phase;
        //t3. La nulidad o no de la lista details de phaseResult;
        //t4. La relacion M-D entre PhaseResult y sus Details

        //Given
        Race race = new Race(1L, "Mock Race", LocalDate.now(), "Montecarlo");
        Phase phase = new Phase();
        phase.setRace(race);
        phase.setPhaseStage(PhaseStage.Q3);
        Driver driver = new Driver(15L, "Driver", "Winner", 1, Team.Mercedes);
        phase.setPhaseResults(new ArrayList<>());


        //When
        when(driverService.getDriverById(15L)).thenReturn(driver);
        when(phaseResultRepository.save(any(PhaseResultEntity.class))).thenReturn(new PhaseResultEntity());
        when(phaseResultDetailRepository.save(any(PhaseResultDetailEntity.class))).thenReturn(new PhaseResultDetailEntity());

        //Then
        PhaseResult result = phaseResultService.recordLap(phase, 15L, Duration.ofSeconds(69));

        assertEquals(Duration.ofSeconds(69), result.getTime());
        assertEquals(15L, result.getDriver().getId());
        assertEquals(1, result.getLaps());



    }

    @Test
    void filterResultFromDriverTest() throws Exception {
        // Given
        Race race = new Race(1L, "Mock Race", LocalDate.now(), "Montecarlo");

        //--Vamos a hacer de cuenta que es una Phase de PhaseStage.Q3, 5 PhaseResults

        Phase phase = new Phase();
        phase.setRace(race);
        phase.setPhaseStage(PhaseStage.Q3);


        //--Crear lista de drivers para asignar uno por cada phaseResult
        List<Driver> driverList = new ArrayList<>();

        driverList.add(new Driver(10L, "Driver", "One", 1, Team.Alpine));
        driverList.add(new Driver(12L, "Driver", "Two", 1, Team.Mercedes)) ;
        driverList.add(new Driver(13L, "Driver", "Two", 1, Team.Ferrari)) ;
        driverList.add( new Driver(14L, "Driver", "Two", 1, Team.Williams)) ;
        driverList.add( new Driver(15L, "Driver", "Two", 1, Team.McLaren));

        List<PhaseResult> phaseResults = new ArrayList<>();
        phase.setPhaseResults(phaseResults);
        for(Driver driver : driverList)
        {
            PhaseResult newPhaseResult = new PhaseResult();
            newPhaseResult.setPhase(phase);
            newPhaseResult.setDriver(driver);
            phase.getPhaseResults().add(newPhaseResult);
        }

        //Then
        PhaseResultServiceImpl testClass = new PhaseResultServiceImpl();
        Method privateMethod = testClass.getClass().getDeclaredMethod("filterResultFromDriver", Phase.class, Long.class);
        privateMethod.setAccessible(true);
        PhaseResult result = (PhaseResult) privateMethod.invoke(testClass, phase, 15L);
        assertNotNull(result);
        assertEquals("Two", result.getDriver().getLastname());


    }

    @Test

    void updatePhaseResultTimeTest() throws Exception {


        // Given
        Race race = new Race(1L, "Mock Race", LocalDate.now(), "Montecarlo");
        //--Vamos a hacer de cuenta que es una Phase de PhaseStage.Q3, 1 PhaseResult de 5 laps

        Phase phase = new Phase();
        phase.setId(1L);
        phase.setRace(race);
        phase.setPhaseStage(PhaseStage.Q3);

        Driver driver = new Driver(15L, "Driver", "Two", 1, Team.McLaren);
        PhaseResult newPhaseResult = new PhaseResult();
        newPhaseResult.setId(15L);
       // newPhaseResult.setPosition(1);

        newPhaseResult.setPhase(phase);
        newPhaseResult.setDriver(driver);
        newPhaseResult.setTime(Duration.ofSeconds(60)); //mejor tiempo inicial, 60 segundos


        List<PhaseResultDetail> phaseResultDetails = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            PhaseResultDetail detail = new PhaseResultDetail();
            detail.setId((long) i+1);
            detail.setPhaseResult(newPhaseResult);
            detail.setDriver(driver);
            detail.setLap(i+1);
            detail.setTime(Duration.ofSeconds(60-i)); //el mejor detail.time tiene que ser el ultimo
            phaseResultDetails.add(detail);
        }
        newPhaseResult.setDetails(phaseResultDetails);
        //newPhaseResult.setLaps(5);
        //When


        /* -- Si fuese metodo publico.

        phaseResultServiceImpl.updatePhaseResultTime(newPhaseResult, phase.getPhaseStage());
        assertEquals(Duration.ofSeconds(56), newPhaseResult.getTime());
        verify(phaseResultRepository, times(1)).save(any(PhaseResultEntity.class));

         */

        Optional<Method> methodOptional = ReflectionSupport.findMethod(PhaseResultServiceImpl.class, "updatePhaseResultTime", PhaseResult.class, PhaseStage.class);
        if(methodOptional.isPresent())
        {
            Method method = methodOptional.get();
            //method.setAccessible(true);
            ReflectionSupport.invokeMethod(method, phaseResultServiceImpl, newPhaseResult, PhaseStage.Q3 ); //Si le paso newPhaseResult.getPhaseStage() no le gusta y rompe
            assertEquals(Duration.ofSeconds(56), newPhaseResult.getTime()); // Se aserta que ahora el mejor tiempo vale 56 segundos despues de actualizar
        }





    }

    @Test
    void calculateNewPositionsTest() {

        //Given

        //Imaginemos una fase Q3 con 5 drivers y 5 resultados donde el de posicion 1 tiene actualmente el peor tiempo
        //Debería pasar a ser posicion 5

        Race race = new Race(1L, "Mock Race", LocalDate.now(), "Montecarlo");
        Phase phase = new Phase();
        phase.setId(1L);
        phase.setRace(race);
        phase.setPhaseStage(PhaseStage.Q3);

        //--Crear lista de drivers para asignar uno por cada phaseResult
        List<Driver> driverList = new ArrayList<>();

        driverList.add(new Driver(10L, "Driver", "One", 1, Team.Alpine));
        driverList.add(new Driver(12L, "Driver", "Two", 1, Team.Mercedes)) ;
        driverList.add(new Driver(13L, "Driver", "Two", 1, Team.Ferrari)) ;
        driverList.add( new Driver(14L, "Driver", "Two", 1, Team.Williams)) ;
        driverList.add( new Driver(15L, "Driver", "Two", 1, Team.McLaren));

        List<PhaseResult> phaseResults = new ArrayList<>();
        phase.setPhaseResults(phaseResults);
        for (int i = 0; i < driverList.size(); i++)
        {
            PhaseResult newPhaseResult = new PhaseResult();
            newPhaseResult.setPhase(phase);
            newPhaseResult.setDriver(driverList.get(i));
            newPhaseResult.setPosition(i+1);
            newPhaseResult.setTime(Duration.ofSeconds(60-i));
            phase.getPhaseResults().add(newPhaseResult);
        }

        //Me aseguro de que el pimero en la lista tenga posicion 1 pero con peor tiempo
        phase.getPhaseResults().get(0).setPosition(1);
        phase.getPhaseResults().get(0).setTime(Duration.ofSeconds(80));
        phase.getPhaseResults().get(0).getDriver().setName("Loser");

        //Then
        Optional<Method> methodOptional = ReflectionSupport.findMethod(PhaseResultServiceImpl.class, "calculateNewPositions", Phase.class, PhaseStage.class );
        if(methodOptional.isPresent())
        {
            Method method = methodOptional.get();
            ReflectionSupport.invokeMethod(method, phaseResultServiceImpl, phase, PhaseStage.Q3);
            assertEquals( Duration.ofSeconds(80) , phase.getPhaseResults().get(4).getTime() );
            assertEquals("Loser", phase.getPhaseResults().get(4).getDriver().getName()  );
        }

    }

}