package com.arquitectura.motor_decisiones.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Opciones_respuesta")
public class OpcionRespuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 500)
    private String textoOpcion;

    @Column(nullable = false)
    private Boolean esCorrecta;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String justificacionFeedback;

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "leccion_id",nullable = false)
    private Leccion leccion;

    public OpcionRespuesta(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTextoOpcion() {
        return textoOpcion;
    }

    public void setTextoOpcion(String textoOpcion) {
        this.textoOpcion = textoOpcion;
    }

    public Boolean getEsCorrecta() {
        return esCorrecta;
    }

    public void setEsCorrecta(Boolean esCorrecta) {
        this.esCorrecta = esCorrecta;
    }

    public String getJustificacionFeedback() {
        return justificacionFeedback;
    }

    public void setJustificacionFeedback(String justificacionFeedback) {
        this.justificacionFeedback = justificacionFeedback;
    }

    public Leccion getLeccion() {
        return leccion;
    }

    public void setLeccion(Leccion leccion) {
        this.leccion = leccion;
    }
}