package ar.edu.utn.frc.tup.lc.iii.dtos;

import ar.edu.utn.frc.tup.lc.iii.config.CustomDurationDeserializer;
import ar.edu.utn.frc.tup.lc.iii.config.CustomDurationSerializer;
import ar.edu.utn.frc.tup.lc.iii.models.Driver;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhaseResultDetailDto {

    private Long id;
    private Driver driver;
    private Integer lap;
    @JsonDeserialize(using = CustomDurationDeserializer.class)
    @JsonSerialize(using = CustomDurationSerializer.class)
    private Duration time;
}
