package ar.edu.utn.frc.tup.lciii.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "materias")
@Data
public class Materia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column
    private Integer calificacion;

    @Column
    private String estado;
}
