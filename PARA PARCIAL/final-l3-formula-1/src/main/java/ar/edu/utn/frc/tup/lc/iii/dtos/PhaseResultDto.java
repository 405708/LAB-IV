package ar.edu.utn.frc.tup.lc.iii.dtos;

import ar.edu.utn.frc.tup.lc.iii.config.CustomDurationDeserializer;
import ar.edu.utn.frc.tup.lc.iii.config.CustomDurationSerializer;
import ar.edu.utn.frc.tup.lc.iii.models.Driver;
import ar.edu.utn.frc.tup.lc.iii.models.PhaseResultDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhaseResultDto {

    private Integer position;
    private Driver driver;
    @JsonDeserialize(using = CustomDurationDeserializer.class)
    @JsonSerialize(using = CustomDurationSerializer.class)
    private Duration time;
    private Integer laps;
    private List<PhaseResultDetailDto> details;
}
