package ar.edu.utn.frc.tup.lc.iii.dtos;

import ar.edu.utn.frc.tup.lc.iii.models.PhaseStage;
import ar.edu.utn.frc.tup.lc.iii.models.PhaseStatus;
import ar.edu.utn.frc.tup.lc.iii.models.Race;
import ar.edu.utn.frc.tup.lc.iii.models.StartingGrid;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhaseDto {

    private Long id;
    private RaceDto race;
    @JsonProperty("phase_stage")
    private PhaseStage phaseStage;
    @JsonProperty("start_date_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDateTime;
    @JsonProperty("end_date_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDateTime;
    @JsonProperty("status")
    private PhaseStatus status;
    @JsonProperty("phase_results")
    private List<PhaseResultDto> phaseResults;

    @JsonProperty("starting_grid")
    private List<StartingGrid> startingGrid;
}
