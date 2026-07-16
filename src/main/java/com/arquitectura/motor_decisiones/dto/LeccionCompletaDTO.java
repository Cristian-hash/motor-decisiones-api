package com.arquitectura.motor_decisiones.dto;

import java.util.List;
/*
* Propósito: Transportar los datos de la lección al Frontend sin
* exponer la estructura de la base de datos ni las respuestas correctas
* */
public record LeccionCompletaDTO(

        Long id,
        String titulo,
        String problemaHook,
        String metafora,
        String pseudocodigo,
        String codigoJava,
        List<OpcionRespuestaDTO> opciones
) {
}
