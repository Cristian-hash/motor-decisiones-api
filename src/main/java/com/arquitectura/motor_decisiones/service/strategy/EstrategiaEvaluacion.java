package com.arquitectura.motor_decisiones.service.strategy;

import com.arquitectura.motor_decisiones.dto.FeedbackDTO;
import com.arquitectura.motor_decisiones.dto.RespuestaEstudianteDTO;
import com.arquitectura.motor_decisiones.entity.Leccion;

public interface EstrategiaEvaluacion {
    // Todas las estrategias del futuro DEBEN tener este método
    FeedbackDTO evaluar(RespuestaEstudianteDTO respuesta, Leccion leccion);
}
