package ar.edu.utn.frc.tup.lciv.controllers;

import ar.edu.utn.frc.tup.lciv.dtos.habitacion.POSTReserva;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.ReservaDTO;
import ar.edu.utn.frc.tup.lciv.services.ReservaService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping()
public class ReservaController {

    @Autowired
    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping("/reserva/{id_reserva}")
    ResponseEntity<ReservaDTO> getReserva(@PathVariable("id_reserva") Long idReserva) {
        ReservaDTO reserva = reservaService.getReserva(idReserva);
        if(reserva == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reserva);
    }


    @PostMapping("/reserva")
    ResponseEntity<ReservaDTO> getReserva(@RequestBody POSTReserva reservaDTO) {
        ReservaDTO reserva = reservaService.createReserva(reservaDTO);
        if(reserva == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reserva);
    }
}
