package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.models.Pool;
import ar.edu.utn.frc.tup.lciii.services.PoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rwc/2023/pools")
@RequiredArgsConstructor
public class PoolController {

    private final PoolService poolService;


    @GetMapping()
    public List<Pool> getPools() {
        return poolService.getPools(null);
    }
    @GetMapping("/{pool_id}")
    public List<Pool> getPoolById(@PathVariable("pool_id") String poolId) {
        return poolService.getPools(poolId);
    }

}
