package com.arquitectura.motor_decisiones.service.gamificacion;

import com.arquitectura.motor_decisiones.entity.Leccion;
import com.arquitectura.motor_decisiones.entity.Progreso;
import com.arquitectura.motor_decisiones.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class PuntosRachaStrategy implements CalculadoraPuntosStrategy {

    @Override
    public int calcularPuntos(Usuario usuario, Leccion leccion){
        return 20;
    }
}