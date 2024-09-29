package ar.edu.utn.frc.tup.lciii.domains;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "numeros_sorteados")

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Sorteo getSorteo() {
        return sorteo;
    }

    public void setSorteo(Sorteo sorteo) {
        this.sorteo = sorteo;
    }
}

