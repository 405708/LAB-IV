package ar.edu.utn.frc.tup.lciii.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelemetryDTO {
    private String ip;

    private String hostname;

    private LocalDateTime dataDate; //TODO hacer string

    private Double hostDiskFree;

    private Double cpuUsage;

    private String microphoneState;

    private Boolean screenCaptureAllowed;

    private Boolean audioCaptureAllowed;
}
