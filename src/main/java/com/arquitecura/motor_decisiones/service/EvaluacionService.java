package com.arquitecura.motor_decisiones.service;

import com.arquitecura.motor_decisiones.dto.FeedbackDTO;
import com.arquitecura.motor_decisiones.dto.RespuestaEstudianteDTO;
import com.arquitecura.motor_decisiones.entity.OpcionRespuesta;
import com.arquitecura.motor_decisiones.repository.OpcionRespuestaRepository;
import org.springframework.stereotype.Service;

@Service
public class EvaluacionService {

    //1RO TRAERME EL REPOSITORY
    private final OpcionRespuestaRepository opcionRepository;

    public EvaluacionService(OpcionRespuestaRepository opcionRepository){
        this.opcionRepository=opcionRepository;
    }

    public FeedbackDTO evaluarDecision(RespuestaEstudianteDTO dto){
        // 1. Buscar la opción que eligió el estudiante en la Base de Datos
        OpcionRespuesta opcionSeleccionada = opcionRepository.findById(dto.opcionSeleccionadaId())
                .orElseThrow(()->new RuntimeException("Error: la opcion seleccionada no existe"));

        // 2. Aplicar la Regla de Negocio (¿Es correcta?)
        boolean esCorrecta = opcionSeleccionada.getEsCorrecta();
        String mensaje = opcionSeleccionada.getJustificacionFeedback();

        //3. Se le empaqueta en un dto y se le envia al front
        return new FeedbackDTO(esCorrecta,mensaje);
    }
}
