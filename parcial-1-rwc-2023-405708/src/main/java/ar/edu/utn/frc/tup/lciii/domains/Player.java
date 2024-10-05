package ar.edu.utn.frc.tup.lciii.domains;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "players")
@Data
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer equipoId;

    @Column
    private String fullname;

    @Column
    private Position position;

    @Column
    private Integer sprint;

    @Column
    private Integer skills;

    @Column
    private Integer shot;

    @Column
    private Integer playerValue;

}
