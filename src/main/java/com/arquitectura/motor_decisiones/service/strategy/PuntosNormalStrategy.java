package com.arquitectura.motor_decisiones.service.strategy;

import com.arquitectura.motor_decisiones.entity.Progreso;
import org.springframework.stereotype.Component;

@Component
public class PuntosNormalStrategy implements CalculadoraPuntosStrategy{
    @Override
    public int calcularPuntos(Progreso progreso) {
        return 10;
    }
}
