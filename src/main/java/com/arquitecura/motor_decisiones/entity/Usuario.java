package com.arquitecura.motor_decisiones.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true,length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name="fecha_registro")
    private LocalDateTime fechaRegistro;

    //  1 Usuario tiene Muchos Progresos
    @OneToMany (mappedBy = "usuario",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Progreso> progresos= new ArrayList<>();

    public Usuario(){
        this.fechaRegistro = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public List<Progreso> getProgresos() {
        return progresos;
    }

    public void setProgresos(List<Progreso> progresos) {
        this.progresos = progresos;
    }
}
