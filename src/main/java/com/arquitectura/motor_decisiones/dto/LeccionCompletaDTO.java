package com.arquitectura.motor_decisiones.dto;

import java.util.List;

public record LeccionCompletaDTO (

        Long id,
        String titulo,
        String problemaHook,
        String metafora,
        String pseudocodigo,
        String codigoJava,
        List<OpcionRespuestaDTO> opciones

){
}
