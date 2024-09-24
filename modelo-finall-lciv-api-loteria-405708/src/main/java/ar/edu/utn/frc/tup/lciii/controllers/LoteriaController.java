package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.ApuestaDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.SaveApuestaDto;
import ar.edu.utn.frc.tup.lciii.services.ApuestaService;
import ar.edu.utn.frc.tup.lciii.services.SorteoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loteria")
public class LoteriaController {

    private final ApuestaService apuestaService;
    private final SorteoService sorteoService;

    public LoteriaController(ApuestaService apuestaService, SorteoService sorteoService) {
        this.apuestaService = apuestaService;
        this.sorteoService = sorteoService;
    }

    @PostMapping("/apuestas")
    public SaveApuestaDto registrarApuesta(ApuestaDto apuestaDto){
        return apuestaService.save(apuestaDto);
    }
}
