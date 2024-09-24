package ar.edu.utn.frc.tup.lciii.domains;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "numeros_sorteados")
@Data
public class NumeroSorteado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer posicion;

    @Column
    private Integer numero;

    @ManyToOne
    @JoinColumn(name = "sorteo_id", nullable = false)
    private Sorteo sorteo; // Relación con Sorteo
}

