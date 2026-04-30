package com.arquitectura.motor_decisiones.dto;

public record RespuestaEstudianteDTO(
        Long usuarioId,
        Long leccionId,
        Long opcionSeleccionadaId
) {
}
