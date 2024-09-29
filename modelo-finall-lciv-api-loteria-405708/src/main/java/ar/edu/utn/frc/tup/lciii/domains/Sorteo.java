package ar.edu.utn.frc.tup.lciii.domains;

import jakarta.persistence.*;
import lombok.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "sorteos")

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
    private Integer dineroTotalAcumulado;

    @Column
    private Integer totalDeApuestas;

    @Column
    private Integer totalPagado;

    @OneToMany(mappedBy = "sorteo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<NumeroSorteado> numerosSorteados = new ArrayList<>(); // Cambiado a NumeroSorteado

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getId_sorteo() {
        return id_sorteo;
    }

    public void setId_sorteo(Integer id_sorteo) {
        this.id_sorteo = id_sorteo;
    }

    public String getFecha_sorteo() {
        return fecha_sorteo;
    }

    public void setFecha_sorteo(String fecha_sorteo) {
        this.fecha_sorteo = fecha_sorteo;
    }

    public List<Apuesta> getApuesta() {
        return apuesta;
    }

    public void setApuesta(List<Apuesta> apuesta) {
        this.apuesta = apuesta;
    }

    public Integer getDineroTotalAcumulado() {
        return dineroTotalAcumulado;
    }

    public void setDineroTotalAcumulado(Integer dineroTotalAcumulado) {
        this.dineroTotalAcumulado = dineroTotalAcumulado;
    }

    public Integer getTotalDeApuestas() {
        return totalDeApuestas;
    }

    public void setTotalDeApuestas(Integer totalDeApuestas) {
        this.totalDeApuestas = totalDeApuestas;
    }

    public Integer getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(Integer totalPagado) {
        this.totalPagado = totalPagado;
    }

    public List<NumeroSorteado> getNumerosSorteados() {
        return numerosSorteados;
    }

    public void setNumerosSorteados(List<NumeroSorteado> numerosSorteados) {
        this.numerosSorteados = numerosSorteados;
    }
}
