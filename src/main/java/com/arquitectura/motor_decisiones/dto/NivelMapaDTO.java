package com.arquitectura.motor_decisiones.dto;

public record NivelMapaDTO(
        Long id,
        String titulo,
        Long leccionInicialId,
        String estado,
        String icono
) {
}
