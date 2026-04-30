package com.arquitectura.motor_decisiones.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    //muchos progresos tienen en 1 leccion,(ejem:el proreso de maria, el progreso de jose,el de pedro en esa leccion)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="leccion_id",nullable=false)
    private Leccion leccion;

    @Column(nullable = false)
    private Boolean completado;

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

    public Leccion getLeccion() {return leccion;}

    public void setLeccion(Leccion leccion) {this.leccion = leccion; }

    public Boolean getCompletado() {return completado;}

    public void setCompletado(Boolean completado) {this.completado = completado;}
}
