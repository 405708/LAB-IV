package ar.edu.utn.frc.tup.lc.iii.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhaseResult {

    private Long id;
    private Phase phase;
    private Integer position;
    private Driver driver;
    private Duration time;
    private Integer laps;
    private List<PhaseResultDetail> details;
}
