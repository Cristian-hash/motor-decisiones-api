package com.arquitectura.motor_decisiones.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        String mensaje,
        int codigoEstado,
        LocalDateTime fecha
) {
}