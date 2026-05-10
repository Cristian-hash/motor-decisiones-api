package com.arquitectura.motor_decisiones.service;

import com.arquitectura.motor_decisiones.dto.LeccionCompletaDTO;
import com.arquitectura.motor_decisiones.dto.OpcionRespuestaDTO;
import com.arquitectura.motor_decisiones.entity.Leccion;
import com.arquitectura.motor_decisiones.exception.RecursoAusenteException;
import com.arquitectura.motor_decisiones.repository.LeccionRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeccionService {
    private final LeccionRepository leccionRepository;
    public LeccionService(LeccionRepository leccionRepository){
        this.leccionRepository=leccionRepository;
    }
    @Transactional(readOnly = true)
    //1 buscar leccion o laznar error
    public LeccionCompletaDTO obtenerLeccionPorId(Long id) {
        //Si no se encuentra la leccion
        Leccion leccion = leccionRepository.findById(id)
                        .orElseThrow(() -> new RecursoAusenteException("Lección no encontrada con ID: " + id));

        // 👇 2. SEGUNDO PARACAÍDAS: ¡ESTO ES LO QUE DEBES ADAPTAR/AGREGAR! 👇
        // Verificamos si la lista de opciones de esta lección está vacía
        if (leccion.getOpciones().isEmpty()) {
            throw new IllegalStateException("Error de integridad: La lección existe, pero carece de opciones configuradas.");
        }

        List<OpcionRespuestaDTO> opcionesDTO = leccion.getOpciones().stream()
                .map(op->new OpcionRespuestaDTO(op.getId(),op.getTextoOpcion()))
                .collect(Collectors.toList());

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