package ar.edu.utn.frc.tup.lc.iii.services.impl;

import ar.edu.utn.frc.tup.lc.iii.entities.PhaseResultEntity;
import ar.edu.utn.frc.tup.lc.iii.entities.StartingGridEntity;
import ar.edu.utn.frc.tup.lc.iii.models.*;
import ar.edu.utn.frc.tup.lc.iii.repositories.StartingGridRepository;
import ar.edu.utn.frc.tup.lc.iii.services.DriverService;
import ar.edu.utn.frc.tup.lc.iii.services.StartingGridService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
class StartingGridServiceImplTest {


    @MockBean
    StartingGridRepository startingGridRepository;

    @MockBean
    DriverService driverService;

    @SpyBean
    StartingGridService startingGridService;

    private Phase startingPhase;
    private List<Driver> startingDrivers;


    private Phase q1Phase;
    private List<PhaseResult> q1PhaseResults;



    @BeforeEach
    void setUp(){
        startingPhase = new Phase();
        startingPhase.setPhaseStage(PhaseStage.Q1);
        startingDrivers = new ArrayList<>();

        for (int i = 0; i <20; i++)
        {
            Driver driver = new Driver();
            driver.setId((long) i);
            driver.setName("Driver " + (i+1));
            startingDrivers.add(driver);
        }

        q1Phase = new Phase();
        q1Phase.setPhaseStage(PhaseStage.Q1);
        q1PhaseResults = new ArrayList<>();

        for (int i = 0; i <20 ; i++)
        {
            PhaseResult phaseResult = new PhaseResult();
            phaseResult.setPhase(q1Phase);
            phaseResult.setDriver(startingDrivers.get(i));
            phaseResult.setPosition(i+1);
            q1PhaseResults.add(phaseResult);
        }

        //La startingPhase y la q1Phase van a compartir los mismos drivers




    }

    @Test
    void createStartingGrid_Test_ArgumentCaptor() {

        //Testear que los Drivers devueltos por el driverService
        //Queden asignados a la startingGridList

        //Given

        when(driverService.getAllDrivers()).thenReturn(startingDrivers);
        List<StartingGrid> expectedStartingGrid = new ArrayList<>();
        for(int i = 0; i < startingDrivers.size(); i++)
        {
            StartingGrid sgOjbect = new StartingGrid();
            sgOjbect.setDriver(startingDrivers.get(i));
            sgOjbect.setPhase(startingPhase);
            sgOjbect.setPosition(i+1);
            expectedStartingGrid.add(sgOjbect);
        }

        //
        startingGridService.createStartingGrid(startingPhase);

        verify(driverService).getAllDrivers();
        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        //TODO Como se utilizaría el ArgCaptor si en vez de un repository.saveAll fuera un .save(Entity) ?
        verify(startingGridRepository).saveAll(argumentCaptor.capture());

        List<StartingGridEntity> outputStartingGrid = (List<StartingGridEntity>) argumentCaptor.getValue();
        assertEquals(expectedStartingGrid.size(), outputStartingGrid.size());

        for(int i = 0; i < expectedStartingGrid.size(); i++)
        {
            //Por mas de que sean de tipos distintos, se puede compara el valor de la propiedad Name
            assertEquals(expectedStartingGrid.get(i).getDriver().getName(), outputStartingGrid.get(i).getDriver().getName() );
        }

        //Nota: En este test se utiliza el ArgumentCaptor de forma ligeramente distinta con respecto al test calculateNextStartingGrid_Q1Phase



    }

    @Test
    @Disabled
    void calculateNextStartingGrid_Q1Phase()
    {

        //Given

        //Elijo 2 drivers cualquiera para tomar como referencia y les seteo un nombre y una posición

        q1PhaseResults.get(5).setPosition(1);
        q1PhaseResults.get(5).getDriver().setLastname("Qualified");
        //Al elemento 5 en la lista le doy posicion 1, deberia ser el primer calificado

        q1PhaseResults.get(19).setPosition(15);
        q1PhaseResults.get(19).getDriver().setLastname("Qualified");
        //Al elemento 20 en la lista le doy posicion 15, debería estar calificado tambien

        q1PhaseResults.get(10).setPosition(16);
        q1PhaseResults.get(10).getDriver().setLastname("Disqualified");
        //Al elemento 10 en la lista le doy posicion 16, D
        // deberia quedar descalificado para la proxima PhaseStage que es Q2;

        q1Phase.setPhaseResults(q1PhaseResults);

        //Then

        //Testear que la starting grid obtenida se guarde como Lista de StartingGridEntity
        //-t1. Con la cantidad correcta de posiciones clasificadas
        //-t2. Con los corredores seteados en las posiciones que clasifican y no clasifican, validando que clasifiquen o no clasifiquen según corresponda

        startingGridService.calculateNextStartingGrid(q1Phase);
        ArgumentCaptor<List<StartingGridEntity>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(startingGridRepository).saveAll(argumentCaptor.capture());
        List<StartingGridEntity> outputStartingGrid = argumentCaptor.getValue();

        //-t1 Si la q1Phase.getResults tiene 20 resultados, el ouput de la siguiente startingGrid tiene que tener size de 15
        assertEquals(15, outputStartingGrid.size());


        //-t2
        //-Intentar encontrar los drivers clasificados "a propósito" por posicion
        List<StartingGridEntity> qualifiedLastname = outputStartingGrid.stream().filter(
                sge -> sge.getDriver().getLastname() != null && sge.getDriver().getLastname().equals("Qualified")
        ).collect(Collectors.toList());

        //Ordeno la List de SG Entity de menor a mayor
        qualifiedLastname.sort(Comparator.comparing(StartingGridEntity::getPosition));

        boolean qualifiedLastnameHavePosition = qualifiedLastname.stream().allMatch( sge -> sge.getPosition().equals(1) || sge.getPosition().equals(15));

        //-Intentar NO encontrar al driver no clasificado "a proposito" por posicion
        boolean disqualifiedLastnameNotFound = qualifiedLastname.stream().noneMatch(sge -> sge.getDriver().getLastname().equals("Disqualfied "));

                assertTrue(qualifiedLastnameHavePosition);
                assertEquals(1, qualifiedLastname.get(0).getPosition());
                assertEquals(15, qualifiedLastname.get(1).getPosition());
                assertTrue(disqualifiedLastnameNotFound);

    }
}