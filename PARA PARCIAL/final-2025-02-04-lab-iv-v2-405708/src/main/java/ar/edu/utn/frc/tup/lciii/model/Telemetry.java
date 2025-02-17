package ar.edu.utn.frc.tup.lciii.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "TELEMETRY")
@EqualsAndHashCode
public class Telemetry {

    @Id
    @SequenceGenerator(name = "telemetry_seq", sequenceName = "TELEMETRY_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "telemetry_seq")
    @Column(name = "ID")
    private Long id;

    @Column
    private String ip;

    @Column
    private LocalDateTime dataDate;

    @Column
    private Double hostDiskFree;

    @Column
    private Double cpuUsage;

    @Column
    private String microphoneState;

    @Column
    private Boolean screenCaptureAllowed;

    @Column
    private Boolean audioCaptureAllowed;

    @Column
    private String hostname;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "HOSTNAME", referencedColumnName = "HOSTNAME", insertable = false, updatable = false)
    private Device device;
}
