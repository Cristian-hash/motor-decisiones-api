package com.arquitectura.motor_decisiones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MotorDecisionesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MotorDecisionesApplication.class, args);
    }

}
/*
 // 1. La herramienta común que todas las estrategias implementan
public interface EstrategiaEvaluacion {
    void evaluar();
    TipoEvaluacion getTipo();
}

// 2. La Fábrica (El Almacén)
@Component
public class EvaluacionStrategyFactory {

    // Aquí guardamos el catálogo de herramientas listas
    private final Map<TipoEvaluacion, EstrategiaEvaluacion> estrategias;

    // Inyectamos todas las estrategias disponibles automáticamente
    public EvaluacionStrategyFactory(List<EstrategiaEvaluacion> listaEstrategias) {
        this.estrategias = new EnumMap<>(TipoEvaluacion.class);

        // Llenamos nuestro catálogo paso a paso
        for (EstrategiaEvaluacion estrategia : listaEstrategias) {
            this.estrategias.put(estrategia.getTipo(), estrategia);
        }
    }

    // El Service usará este método para pedir la herramienta
    public EstrategiaEvaluacion obtenerEstrategia(TipoEvaluacion tipo) {
        return estrategias.get(tipo);
    }
}


 */
