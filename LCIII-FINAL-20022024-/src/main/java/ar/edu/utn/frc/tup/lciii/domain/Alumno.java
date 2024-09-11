package ar.edu.utn.frc.tup.lciii.domain;

import lombok.Data;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "alumnos")
@Data
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String legajo;

    @Column
    private String nombre;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "materia_id")
    private List<Materia> materias;
}
