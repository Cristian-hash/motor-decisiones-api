package com.arquitectura.motor_decisiones.dto;

public record RespuestaEstudianteDTO(
        //Solo lo que el estudiante envia
        Long usuarioId,
        Long leccionId,
        Long opcionSeleccionadaId
) {
}
