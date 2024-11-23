package ar.edu.utn.frc.tup.lc.iii.models;

import lombok.*;

import java.time.Duration;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class PhaseResultDetail {

    private Long id;
    private Driver driver;
    private Integer lap;
    private Duration time;
    private PhaseResult phaseResult;
}
