package com.arquitecura.motor_decisiones.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="progresos")
public class Progreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer puntajeObtenido;

    @Column(nullable = false)
    private String nivelAlcanzado;

    private LocalDateTime fechaIntento;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="usuario_id",nullable = false)
    private Usuario usuario;

    public Progreso(){
        this.fechaIntento=LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPuntajeObtenido() {
        return puntajeObtenido;
    }

    public void setPuntajeObtenido(Integer puntajeObtenido) {
        this.puntajeObtenido = puntajeObtenido;
    }

    public String getNivelAlcanzado() {
        return nivelAlcanzado;
    }

    public void setNivelAlcanzado(String nivelAlcanzado) {
        this.nivelAlcanzado = nivelAlcanzado;
    }

    public LocalDateTime getFechaIntento() {
        return fechaIntento;
    }

    public void setFechaIntento(LocalDateTime fechaIntento) {
        this.fechaIntento = fechaIntento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
