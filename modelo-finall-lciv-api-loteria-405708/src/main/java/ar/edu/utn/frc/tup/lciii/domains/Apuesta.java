package ar.edu.utn.frc.tup.lciii.domains;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "apuestas")
@Data
public class Apuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fecha_sorteo;

    @Column
    private String id_cliente;

    //Consultar tipo
    @Column
    private String numero;

    //Campos null al iniciar
    @Column
    private Integer montoApostado;

    @Column
    private Resultado resultado;

    @Column
    private Integer premio;
}
