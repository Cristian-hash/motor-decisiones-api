package com.arquitecura.motor_decisiones.repository;

import com.arquitecura.motor_decisiones.entity.Progreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgresoRepository extends JpaRepository<Progreso,Long> {
}
