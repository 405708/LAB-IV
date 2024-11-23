package ar.edu.utn.frc.tup.lc.iii.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RaceDto {

    private Long id;
    @NotNull(message = "name can´t by null")
    private String name;
    @NotNull(message = "circuit can´t by null")
    private String circuit;
    @JsonProperty("race_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate raceDate;
}
