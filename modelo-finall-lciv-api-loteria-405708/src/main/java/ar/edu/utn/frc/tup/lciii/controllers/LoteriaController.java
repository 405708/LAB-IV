package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.ApuestaDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.SaveApuestaDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.SorteoBetsDto;
import ar.edu.utn.frc.tup.lciii.services.ApuestaService;
import ar.edu.utn.frc.tup.lciii.services.SorteoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

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
    public ResponseEntity<SaveApuestaDto> registrarApuesta(@RequestBody ApuestaDto apuestaDto){
        SaveApuestaDto result = apuestaService.save(apuestaDto);
        if(result == null){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/sorteo/{id_sorteo}")
    public ResponseEntity<SorteoBetsDto> GetSorteoWithApuestas (@PathVariable("id_sorteo") Integer id){
        SorteoBetsDto sorteo = sorteoService.getByIdWithBet(id);
        if(sorteo == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(sorteo);
    }
}
