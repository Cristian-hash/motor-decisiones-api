package com.arquitectura.motor_decisiones.service.strategy;

import com.arquitectura.motor_decisiones.entity.Progreso;

public interface CalculadoraPuntosStrategy {

    /*
    * Calcula los puntos obtenidos en base al progreso y contexto del alumno
    * @Param progreso ,El inteneto actual del usuario
    * @return Cantidad de puntos obtenidos
    * */

    int calcularPuntos(Progreso progreso);

}
