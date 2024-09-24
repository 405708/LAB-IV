package ar.edu.utn.frc.tup.lciii.domains;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "numerosSorteados")
@Data
public class NumeroSorteado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer posicion;

    @Column
    private Integer numero;
}
