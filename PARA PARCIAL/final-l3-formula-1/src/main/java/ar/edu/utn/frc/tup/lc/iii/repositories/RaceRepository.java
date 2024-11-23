package ar.edu.utn.frc.tup.lc.iii.repositories;

import ar.edu.utn.frc.tup.lc.iii.entities.RaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RaceRepository extends JpaRepository<RaceEntity, Long> {

}
