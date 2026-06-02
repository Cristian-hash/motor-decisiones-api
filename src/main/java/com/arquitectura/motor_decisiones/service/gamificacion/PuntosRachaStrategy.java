package com.arquitectura.motor_decisiones.service.gamificacion;

import com.arquitectura.motor_decisiones.entity.Progreso;
import org.springframework.stereotype.Component;

@Component
public class PuntosRachaStrategy implements CalculadoraPuntosStrategy {

    @Override
    public int calcularPuntos(Progreso progreso){
        return 20;
    }
}