package ar.edu.utn.frc.tup.lc.iii.repositories;

import ar.edu.utn.frc.tup.lc.iii.entities.PhaseResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhaseResultRepository extends JpaRepository<PhaseResultEntity, Long> {

}
