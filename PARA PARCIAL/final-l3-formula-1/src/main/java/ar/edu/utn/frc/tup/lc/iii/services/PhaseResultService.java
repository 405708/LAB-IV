package ar.edu.utn.frc.tup.lc.iii.services;

import ar.edu.utn.frc.tup.lc.iii.models.Phase;
import ar.edu.utn.frc.tup.lc.iii.models.PhaseResult;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public interface PhaseResultService {

    PhaseResult recordLap(Phase phase, Long driverId, Duration time);
}
