package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.*;
import ar.edu.utn.frc.tup.lc.iv.services.EleccionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/elecciones")
public class EleccionesController {

    @Autowired
    private final EleccionesService eleccionesService;

    public EleccionesController(EleccionesService eleccionesService){
        this.eleccionesService = eleccionesService;
    }

    @GetMapping("/distritos")
    public ResponseEntity<List<DistritoDTO>> getDistritos(@RequestParam(required = false, value = "distrito_name") String nombre) {
       List<DistritoDTO> distritos = eleccionesService.getDistritosAllOrByName(nombre);
       if(distritos.isEmpty()){
           return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(distritos);
    }

    @GetMapping("/distritos/{id}")
    public ResponseEntity<DistritoDTO> getDistritoById(@PathVariable Long id) {
        DistritoDTO distrito = eleccionesService.getDistritoById(id);
        if(distrito == null){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(distrito);
    }

    //TODO
    @GetMapping("/distritos/{id}/cargos")
    public ResponseEntity<List<CargoDTO>> getCargosDistrito(@PathVariable Long id) {
        List<CargoDTO> cargos = eleccionesService.getCargosDistrito(id);
        if(cargos.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cargos);
    }

    //TODO
    @GetMapping("/distritos/{distritoId}/cargos/{cargoId}")
    public ResponseEntity<CargoDTO> getCargoByIdAndDistritoId(@PathVariable Long distritoId,
                                                              @PathVariable Long cargoId) {
        CargoDTO cargoDTO = eleccionesService.getCargoByIdAndDistritoId(distritoId, cargoId);
        if(cargoDTO == null){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cargoDTO);
    }

    //TODO
    @GetMapping("/distritos/{id}/secciones")
    public ResponseEntity<List<SeccionDTO>> getSeccionesByDistrito(@PathVariable Long id) {
        List<SeccionDTO> seccionDTOS = eleccionesService.getSeccionesByDistrito(id);
        if(seccionDTOS.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(seccionDTOS);
    }

    //TODO
    @GetMapping("/distritos/{distritoId}/secciones/{seccionId}")
    public ResponseEntity<SeccionDTO> getSeccionByIdAndDistritoId(@PathVariable Long distritoId,
                                                                 @PathVariable Long seccionId) {
        SeccionDTO seccionDTO = eleccionesService.getSeccionByIdAndDistritoId(distritoId, seccionId);
        if(seccionDTO == null){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(seccionDTO);
    }



    //TODO
    @GetMapping("/distritos/{id}/resultados")
    public ResponseEntity<ResultadoDistritoDTO> getResultadosByDistrito(@PathVariable Long id) {
        ResultadoDistritoDTO resultado = eleccionesService.getResultadosByDistrito(id);
        if(resultado == null){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultado);
    }

    //TODO
    @GetMapping("/resultados")
    public ResponseEntity<ResultadosDTO> getResultadosGenerales() {
        ResultadosDTO resultado = eleccionesService.getResultadosGenerales();
        if(resultado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultado);
    }


}
