package com.arquitectura.motor_decisiones.service.gamificacion;

import com.arquitectura.motor_decisiones.entity.Leccion;
import com.arquitectura.motor_decisiones.entity.Progreso;
import com.arquitectura.motor_decisiones.entity.Usuario;

public interface CalculadoraPuntosStrategy {

    /*
    * Calcula los puntos obtenidos en base al progreso y contexto del alumno
    * @Param progreso ,El inteneto actual del usuario
    * @return Cantidad de puntos obtenidos
    * */
    int calcularPuntos(Usuario usuario, Leccion leccion);
}