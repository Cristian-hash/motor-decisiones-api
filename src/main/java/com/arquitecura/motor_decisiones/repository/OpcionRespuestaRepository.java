package com.arquitecura.motor_decisiones.repository;

import com.arquitecura.motor_decisiones.entity.OpcionRespuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpcionRespuestaRepository extends JpaRepository<OpcionRespuesta,Long> {
}
