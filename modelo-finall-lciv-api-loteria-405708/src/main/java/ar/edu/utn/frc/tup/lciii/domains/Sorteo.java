package ar.edu.utn.frc.tup.lciii.domains;

import jakarta.persistence.*;
import lombok.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sorteos")
@Data
public class Sorteo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer id_sorteo;

    @Column
    private String fecha_sorteo;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "apuesta_id")
    private List<Apuesta> apuesta;

    //Campos a rellenar
    @Column
    private Integer totalEnReserva;

    @Column
    private Integer totalDeApuestas;

    @Column
    private Integer totalPagado;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "numero_sorteado_id")
    private List<NumeroSorteado> numerosSorteados;

}
