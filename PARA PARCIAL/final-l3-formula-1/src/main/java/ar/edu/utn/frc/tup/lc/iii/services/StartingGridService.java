package ar.edu.utn.frc.tup.lc.iii.services;

import ar.edu.utn.frc.tup.lc.iii.models.Phase;
import org.springframework.stereotype.Service;

@Service
public interface StartingGridService {

    void createStartingGrid(Phase phase);
    void calculateNextStartingGrid(Phase phase);
}
