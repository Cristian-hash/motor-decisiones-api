package com.arquitectura.motor_decisiones.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "progresos", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_usuario_leccion_completado",
                columnNames = {"usuario_id", "leccion_id", "completado"}
        )
})
public class Progreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,updatable = false)
    private Integer puntajeObtenido;

    @Column(nullable = false)
    private String nivelAlcanzado;

    private LocalDateTime fechaIntento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    //muchos progresos tienen en 1 leccion,(ejem:el proreso de maria, el progreso de jose,el de pedro en esa leccion)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leccion_id", nullable = false)
    private Leccion leccion;

    @Column(nullable = false)
    private Boolean completado;

    // 1. Constructor vacío protegido (JPA lo requiere, pero nosotros no lo usaremos directamente)
    protected Progreso() {
    }

    // 2. Constructor de Evento: Exige todos los datos al momento de crear el registro
    public Progreso(Integer puntajeObtenido, String nivelAlcanzado, Usuario usuario, Leccion leccion, Boolean completado) {
        this.puntajeObtenido = puntajeObtenido;
        this.nivelAlcanzado = nivelAlcanzado;
        this.usuario = usuario;
        this.leccion = leccion;
        this.completado = completado;
        this.fechaIntento = LocalDateTime.now(); // La fecha se fija al nacer, inmutable.
    }
    // 3. ¡SOLO GETTERS! Eliminamos todos los setters.
    // Nadie podrá modificar este objeto una vez instanciado.
    public Long getId() {
        return id;
    }
    public Integer getPuntajeObtenido() {
        return puntajeObtenido;
    }
    public String getNivelAlcanzado() {
        return nivelAlcanzado;
    }
    public LocalDateTime getFechaIntento() {
        return fechaIntento;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public Leccion getLeccion() {
        return leccion;
    }
    public Boolean getCompletado() {
        return completado;
    }
}
