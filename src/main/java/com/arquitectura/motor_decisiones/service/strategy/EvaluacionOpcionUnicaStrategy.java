package com.arquitectura.motor_decisiones.service.strategy;


import com.arquitectura.motor_decisiones.dto.FeedbackDTO;
import com.arquitectura.motor_decisiones.dto.RespuestaEstudianteDTO;
import com.arquitectura.motor_decisiones.entity.Leccion;
import com.arquitectura.motor_decisiones.entity.OpcionRespuesta;
import com.arquitectura.motor_decisiones.enums.TipoEvaluacion;
import org.springframework.stereotype.Component;

@Component
public class EvaluacionOpcionUnicaStrategy implements EstrategiaEvaluacion {

    @Override
    public TipoEvaluacion getTipo() {
        return TipoEvaluacion.OPCION_UNICA;
    }

    @Override
    public FeedbackDTO evaluar(RespuestaEstudianteDTO respuesta, Leccion leccion) {

        // 1. Buscamos la opción exacta que el estudiante seleccionó dentro de la lección
        OpcionRespuesta opcionSeleccionada = leccion.getOpciones().stream()
                .filter(opcion -> opcion.getId().equals(respuesta.opcionSeleccionadaId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("La opción seleccionada no se encuentra en la lección"));
        // 2. Evaluamos si es correcta usando TU getter
        boolean esCorrecta = opcionSeleccionada.getEsCorrecta();
        // 3. Extraemos el feedback específico usando TU getter
        String mensajeFeedback = opcionSeleccionada.getJustificacionFeedback();

        // 4. Calculamos puntaje
        int puntosObtenidos = esCorrecta ? leccion.getPuntosRecompensa() : 0;
        // 5. Generamos el "Consejo Inteligente" (Inteligencia de negocio)
        String consejoSiguientePaso = esCorrecta
                ? "¡Excelente decisión! Tienes luz verde para avanzar al siguiente patrón de diseño."
                : "Te recomendamos repasar los diagramas de esta lección antes de intentar tomar otra decisión.";
        // 5. Retornamos el DTO
        return new FeedbackDTO(
                esCorrecta,
                mensajeFeedback,
                puntosObtenidos,
                consejoSiguientePaso
        );
    }
}
