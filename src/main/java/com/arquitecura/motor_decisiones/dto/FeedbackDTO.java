package com.arquitecura.motor_decisiones.dto;

public record FeedbackDTO(
        boolean esCorrecto,
        String mensajeJustificacion
) {
}
