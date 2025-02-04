package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.dtos.habitacion.POSTReserva;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.ReservaDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReservaService {
    ReservaDTO createReserva(POSTReserva reserva);
    ReservaDTO getReserva(Long id);
}
