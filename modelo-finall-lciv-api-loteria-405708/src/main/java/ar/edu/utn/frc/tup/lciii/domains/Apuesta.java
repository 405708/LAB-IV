package ar.edu.utn.frc.tup.lciii.domains;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "apuestas")

public class Apuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fechaSorteo;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFecha_sorteo() {
        return fechaSorteo;
    }

    public void setFecha_sorteo(String fecha_sorteo) {
        this.fechaSorteo = fecha_sorteo;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Integer getMontoApostado() {
        return montoApostado;
    }

    public void setMontoApostado(Integer montoApostado) {
        this.montoApostado = montoApostado;
    }

    public Resultado getResultado() {
        return resultado;
    }

    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
    }

    public Integer getPremio() {
        return premio;
    }

    public void setPremio(Integer premio) {
        this.premio = premio;
    }
}
