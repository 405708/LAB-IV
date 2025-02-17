package ar.edu.utn.frc.tup.lciii.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllTelemetryDTO {

    private String ip;

    private LocalDateTime dataDate; //TODO hacer string

    private Double hostDiskFree;

    private String microphoneState;

    private Boolean screenCaptureAllowed;

    private Boolean audioCaptureAllowed;

    private String hostname;
}
