package com.arquitectura.motor_decisiones.service;

import com.arquitectura.motor_decisiones.dto.LeccionCompletaDTO;
import com.arquitectura.motor_decisiones.dto.OpcionRespuestaDTO;
import com.arquitectura.motor_decisiones.entity.Leccion;
import com.arquitectura.motor_decisiones.exception.RecursoAusenteException;
import com.arquitectura.motor_decisiones.repository.LeccionRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeccionService {
    private final LeccionRepository leccionRepository;
    private final KafkaTemplate<String,String> kafkaTemplate;

    public LeccionService(
            LeccionRepository leccionRepository,
            KafkaTemplate kafkaTemplate
    ) {
        this.leccionRepository = leccionRepository;
        this.kafkaTemplate=kafkaTemplate;
    }

    @Transactional(readOnly = true)
    public LeccionCompletaDTO obtenerLeccionPorId(Long id) {
        // Buscar la lección o lanzar excepción si no existe
        Leccion leccion = leccionRepository.findById(id)
                .orElseThrow(() -> new RecursoAusenteException("Lección no encontrada con ID: " + id));
        // Regla de negocio:
        // una lección válida debe contener opciones configuradas
        if (leccion.getOpciones().isEmpty()) {
            throw new IllegalStateException("Error de integridad: La lección existe, pero carece de opciones configuradas.");
        }
        // Transformar entidades en DTOs para exponer solo
        // la información necesaria hacia el cliente
        List<OpcionRespuestaDTO> opcionesDTO = leccion.getOpciones().stream()
                .map(op -> new OpcionRespuestaDTO(op.getId(), op.getTextoOpcion()))
                .collect(Collectors.toList());
        // Construir respuesta completa lista para la API
        return new LeccionCompletaDTO(
                leccion.getId(),
                leccion.getTitulo(),
                leccion.getProblemaHook(),
                leccion.getMetafora(),
                leccion.getPseudocodigo(),
                leccion.getCodigoJava(),
                opcionesDTO
        );
    }
}
//ESTE DIA ORGANIZE MI CODIGO EN FUNCION DE LO DE APRENDIZAJE NEFOCADO EN COMPRENDER