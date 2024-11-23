package ar.edu.utn.frc.tup.lc.iii.dtos;

import ar.edu.utn.frc.tup.lc.iii.config.CustomDurationDeserializer;
import ar.edu.utn.frc.tup.lc.iii.config.CustomDurationSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LapRecordRequestDto {

    @JsonProperty("driver_id")
    private Long driverId;
    @JsonDeserialize(using = CustomDurationDeserializer.class)
    @JsonSerialize(using = CustomDurationSerializer.class)
    private Duration time;
}
