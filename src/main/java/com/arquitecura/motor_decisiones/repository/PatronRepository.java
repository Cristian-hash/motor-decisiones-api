package com.arquitecura.motor_decisiones.repository;

import com.arquitecura.motor_decisiones.entity.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatronRepository extends JpaRepository <Patron,Long> {
    Optional<Patron> findById (String nombre);
}
