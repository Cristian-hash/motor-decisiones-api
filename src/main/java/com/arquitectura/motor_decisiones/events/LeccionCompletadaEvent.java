package com.arquitectura.motor_decisiones.events;

public record LeccionCompletadaEvent(
        Long usuarioId,
        Long leccionId,
        int puntosGanados
) {
}