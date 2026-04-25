package com.arquitecura.motor_decisiones.repository;

import com.arquitecura.motor_decisiones.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Optional <Usuario> findByEmail(String email);
}
