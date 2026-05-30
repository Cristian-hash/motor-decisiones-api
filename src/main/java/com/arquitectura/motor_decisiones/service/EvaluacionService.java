package com.arquitectura.motor_decisiones.service;

import com.arquitectura.motor_decisiones.dto.FeedbackDTO;
import com.arquitectura.motor_decisiones.dto.RespuestaEstudianteDTO;
import com.arquitectura.motor_decisiones.entity.Leccion;
import com.arquitectura.motor_decisiones.entity.Progreso;
import com.arquitectura.motor_decisiones.entity.Usuario;
import com.arquitectura.motor_decisiones.enums.TipoEvaluacion;
import com.arquitectura.motor_decisiones.exception.LeccionYaCompletadaException;
import com.arquitectura.motor_decisiones.exception.RecursoNoEncontradoException;
import com.arquitectura.motor_decisiones.repository.LeccionRepository;
import com.arquitectura.motor_decisiones.repository.ProgresoRepository;
import com.arquitectura.motor_decisiones.repository.UsuarioRepository;
import com.arquitectura.motor_decisiones.service.strategy.EstrategiaEvaluacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EvaluacionService {
    //1 Agenda de especialistas
    private final EvaluacionStrategyFactory factory;
    private final UsuarioRepository usuarioRepository;
    private final LeccionRepository leccionRepository;
    private final ProgresoRepository progresoRepository;

    @Autowired
    public EvaluacionService(
            EvaluacionStrategyFactory factory,
            UsuarioRepository usuarioRepository,
            LeccionRepository leccionRepository,
            ProgresoRepository progresoRepository
    ) {
        this.factory = factory;
        this.usuarioRepository = usuarioRepository;
        this.leccionRepository = leccionRepository;
        this.progresoRepository = progresoRepository;
    }
    @Transactional // Garantiza que si falla el guardado, no haya datos inconsistentes
    public FeedbackDTO evaluarDecision(RespuestaEstudianteDTO dto) {
        //REGLA ANTIFRAUDE
        boolean yaAprobo = progresoRepository.existsByUsuarioIdAndLeccionIdAndCompletadoTrue(
                dto.usuarioId(), dto.leccionId());

        if (yaAprobo) {
            throw new LeccionYaCompletadaException(
                    "FRAUDE DETECTADO: El usuario " + dto.usuarioId() + "ya completo con éxito la lección" + dto.leccionId() + ". No se permiten puntos duplicados."
            );
        }

        // 2. Extraer dato
        Usuario usuario = usuarioRepository.findById(dto.usuarioId()).
                orElseThrow(() -> new RecursoNoEncontradoException("Error: opcion con id " + dto.usuarioId() + " no encontrada"));

        Leccion leccion = leccionRepository.findById(dto.leccionId()).
                orElseThrow(() -> new RecursoNoEncontradoException("Error: opcion con id " + dto.leccionId() + " no encontrada"));

        // 3. ORQUESTAR: El Service pide la herramienta a la Fábrica
            EstrategiaEvaluacion estrategia = factory.obtenerEstrategia(leccion.getTipoEvaluacion());

        // 4. DELEGAR: El Service ejecuta la herramienta
        FeedbackDTO feedback = estrategia.evaluar(dto, leccion);

        // 5. GUARDAR PROGRESO (Intacto)
        Progreso nuevoProgreso = new Progreso();
        nuevoProgreso.setUsuario(usuario);
        nuevoProgreso.setLeccion(leccion);
        nuevoProgreso.setFechaIntento(LocalDateTime.now());
        nuevoProgreso.setCompletado(feedback.esCorrecto());
        nuevoProgreso.setPuntajeObtenido(feedback.esCorrecto() ? leccion.getPuntosRecompensa() : 0);
        nuevoProgreso.setNivelAlcanzado("Principiante");
        progresoRepository.save(nuevoProgreso);
        return feedback;
    }
}
