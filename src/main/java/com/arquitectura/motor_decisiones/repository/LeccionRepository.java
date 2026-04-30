package com.arquitectura.motor_decisiones.repository;

import com.arquitectura.motor_decisiones.entity.Leccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeccionRepository extends JpaRepository<Leccion,Long> {
}
