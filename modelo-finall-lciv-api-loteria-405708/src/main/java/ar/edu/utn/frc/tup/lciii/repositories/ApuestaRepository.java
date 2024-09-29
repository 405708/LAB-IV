package ar.edu.utn.frc.tup.lciii.repositories;

import ar.edu.utn.frc.tup.lciii.domains.Apuesta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApuestaRepository extends JpaRepository<Apuesta, Long> {
    List<Apuesta> getApuestasByFechaSorteo(String fecha);
}
