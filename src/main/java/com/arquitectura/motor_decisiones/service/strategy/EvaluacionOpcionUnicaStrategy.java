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
    public TipoEvaluacion getTipo(){
        return TipoEvaluacion.OPCION_UNICA;
    }

    @Override
    public FeedbackDTO evaluar(RespuestaEstudianteDTO respuesta, Leccion leccion){

        // 1. Buscamos la opción exacta que el estudiante seleccionó dentro de la lección
        OpcionRespuesta opcionseleccionada = leccion.getOpciones().stream()
                .filter(opcion->opcion.getId().equals(respuesta.opcionSeleccionadaId()))
                .findFirst()
                .orElseThrow(()->new IllegalArgumentException("La opción seleccionada no se encuentra en la lección"));

        // 2. Evaluamos si es correcta usando TU getter
        boolean esCorrecta = opcionseleccionada.getEsCorrecta();
        // 3. Extraemos el feedback específico usando TU getter
        String mensajeFeedback=opcionseleccionada.getJustificacionFeedback();

        // 4. Calculamos puntaje
        int puntosObtenidos =  esCorrecta ? leccion.getPuntosRecompensa() : 0;
        // 5. Retornamos el DTO
        return new FeedbackDTO(
                esCorrecta,
                mensajeFeedback
        );
    }
}
