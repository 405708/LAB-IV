package ar.edu.utn.frc.tup.lciii.repositories;

import ar.edu.utn.frc.tup.lciii.domains.Sorteo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SorteoRepository extends JpaRepository<Sorteo, Long> {
}
