package com.arquitectura.motor_decisiones.repository;

import com.arquitectura.motor_decisiones.entity.Progreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
public interface ProgresoRepository extends JpaRepository<Progreso, Long> {
    boolean existsByUsuarioIdAndLeccionIdAndCompletadoTrue(Long usuarioId, Long leccionId);
    @Query("SELECT COALESCE(MAX(p.leccion.id), 0) " +
            "FROM Progreso p " +
            "WHERE p.usuario.id = :usuarioId " +
            "AND p.completado = true")
    Long findUltimaLeccionCompletada(@Param("usuarioId") Long usuarioId);
}
