package com.arquitectura.motor_decisiones.dto;

public record FeedbackDTO(
        boolean esCorrecto,
        String mensajeJustificacion
) {
}
