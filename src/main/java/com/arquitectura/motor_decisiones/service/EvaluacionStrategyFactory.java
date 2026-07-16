package com.arquitectura.motor_decisiones.service;

import com.arquitectura.motor_decisiones.enums.TipoEvaluacion;
import com.arquitectura.motor_decisiones.exception.LeccionYaCompletadaException;
import com.arquitectura.motor_decisiones.service.strategy.EstrategiaEvaluacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
//Patron Factory
public class EvaluacionStrategyFactory {
    private final Map<TipoEvaluacion, EstrategiaEvaluacion> estrategias;

    @Autowired // Spring Boot inyecta mágicamente TODAS las clases que implementen la interfaz
    public EvaluacionStrategyFactory(List<EstrategiaEvaluacion> estrategiaList){
        // Convertimos la lista inyectada en un mapa para búsqueda ultrarrápida O(1)
     this.estrategias= estrategiaList.stream()
             .collect(Collectors.toMap(
                     estrategia -> estrategia.getTipo(),
                     estrategia->estrategia
             ));
    }

    public EstrategiaEvaluacion obtenerEstrategia(TipoEvaluacion tipo){
        EstrategiaEvaluacion estrategia = estrategias.get(tipo);
        if(estrategia == null){
            throw new LeccionYaCompletadaException("No hay estrategia configurada para el tipo:"+ tipo);
        }
        return estrategia;
    }
}