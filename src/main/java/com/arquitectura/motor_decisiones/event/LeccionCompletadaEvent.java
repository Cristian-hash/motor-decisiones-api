package com.arquitectura.motor_decisiones.event;

public record LeccionCompletadaEvent(
        Long usuarioId,
        int puntosGanados
) {
}
