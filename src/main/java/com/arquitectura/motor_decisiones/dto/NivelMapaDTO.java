package com.arquitectura.motor_decisiones.dto;

import java.util.List;

public record NivelMapaDTO(
        Long id,
        String titulo,
        Long leccionInicialId,
        String estado,
        String icono,
        List <Long> rutaLecciones
) {
}
