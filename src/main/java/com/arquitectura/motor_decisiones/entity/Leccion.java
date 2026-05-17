package com.arquitectura.motor_decisiones.entity;

import com.arquitectura.motor_decisiones.enums.TipoEvaluacion;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="lecciones")
public class Leccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String problemaHook;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String metafora;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String pseudocodigo;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String codigoJava;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patron_id",nullable = false)
    private Patron patron;

    @Column(nullable = false, columnDefinition = "int default 10")
    private Integer puntosRecompensa =10;

    @OneToMany(mappedBy = "leccion",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private List<OpcionRespuesta> opciones = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name="tipo_evaluacion",nullable=false)
    private TipoEvaluacion tipoEvaluacion = TipoEvaluacion.OPCION_UNICA;





    public Leccion(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getProblemaHook() {
        return problemaHook;
    }

    public void setProblemaHook(String problemaHook) {
        this.problemaHook = problemaHook;
    }

    public String getMetafora() {
        return metafora;
    }

    public void setMetafora(String metafora) {
        this.metafora = metafora;
    }

    public String getPseudocodigo() {
        return pseudocodigo;
    }

    public void setPseudocodigo(String pseudocodigo) {
        this.pseudocodigo = pseudocodigo;
    }

    public String getCodigoJava() {
        return codigoJava;
    }

    public void setCodigoJava(String codigoJava) {
        this.codigoJava = codigoJava;
    }

    public Patron getPatron() {
        return patron;
    }

    public void setPatron(Patron patron) {
        this.patron = patron;
    }

    public List<OpcionRespuesta> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<OpcionRespuesta> opciones) {
        this.opciones = opciones;
    }

    public Integer getPuntosRecompensa() {return puntosRecompensa;
    }
    public void setPuntosRecompensa(Integer puntosRecompensa) {this.puntosRecompensa = puntosRecompensa;
    }

    public TipoEvaluacion getTipoEvaluacion() {
        return tipoEvaluacion;
    }

    public void setTipoEvaluacion(TipoEvaluacion tipoEvaluacion) {
        this.tipoEvaluacion = tipoEvaluacion;
    }

}