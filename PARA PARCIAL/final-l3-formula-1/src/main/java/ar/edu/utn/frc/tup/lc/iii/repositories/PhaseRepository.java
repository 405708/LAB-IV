package ar.edu.utn.frc.tup.lc.iii.repositories;

import ar.edu.utn.frc.tup.lc.iii.entities.PhaseEntity;
import ar.edu.utn.frc.tup.lc.iii.models.PhaseStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhaseRepository extends JpaRepository<PhaseEntity, Long> {

    Optional<List<PhaseEntity>> findAllByRaceId(Long raceId);
    Optional<PhaseEntity> findByRaceIdAndPhaseStage(Long race_id, PhaseStage phaseStage);
}
