package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.ApuestaDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.SaveApuestaDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.SorteoBetsDto;
import ar.edu.utn.frc.tup.lciii.services.ApuestaService;
import ar.edu.utn.frc.tup.lciii.services.SorteoService;
import org.springframework.web.bind.annotation.*;

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
    public SaveApuestaDto registrarApuesta(@RequestBody ApuestaDto apuestaDto){
        return apuestaService.save(apuestaDto);
    }

    @GetMapping("/sorteo/{id_sorteo}")
    public SorteoBetsDto GetSorteoWithApuestas (@PathVariable("id_sorteo") Integer id){
        SorteoBetsDto sorteo = sorteoService.getByIdWithBet(id);
        if(sorteo == null){
            return null;
        }
        return sorteo;
    }
}
