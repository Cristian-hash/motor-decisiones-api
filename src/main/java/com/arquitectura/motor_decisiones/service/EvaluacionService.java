package com.arquitectura.motor_decisiones.service;

import com.arquitectura.motor_decisiones.dto.FeedbackDTO;
import com.arquitectura.motor_decisiones.dto.RespuestaEstudianteDTO;
import com.arquitectura.motor_decisiones.entity.Leccion;
import com.arquitectura.motor_decisiones.entity.Progreso;
import com.arquitectura.motor_decisiones.entity.Usuario;
import com.arquitectura.motor_decisiones.events.LeccionCompletadaEvent;
import com.arquitectura.motor_decisiones.exception.LeccionYaCompletadaException;
import com.arquitectura.motor_decisiones.exception.RecursoNoEncontradoException;
import com.arquitectura.motor_decisiones.repository.LeccionRepository;
import com.arquitectura.motor_decisiones.repository.ProgresoRepository;
import com.arquitectura.motor_decisiones.repository.UsuarioRepository;
import com.arquitectura.motor_decisiones.service.gamificacion.CalculadoraPuntosStrategy;
import com.arquitectura.motor_decisiones.service.strategy.EstrategiaEvaluacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;


@Service
public class EvaluacionService {
    //1 Agenda de especialistas
    private final EvaluacionStrategyFactory factory;
    public final CalculadoraPuntosStrategy estrategiaPuntos;
    private final UsuarioRepository usuarioRepository;
    private final LeccionRepository leccionRepository;
    private final ProgresoRepository progresoRepository;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public EvaluacionService(
            EvaluacionStrategyFactory factory,
            // Inyectamos por defecto la estrategia normal usando @Qualifier si se tiene varias
            @Qualifier("puntosNormalStrategy") CalculadoraPuntosStrategy estrategiaPuntos,
            UsuarioRepository usuarioRepository,
            LeccionRepository leccionRepository,
            ProgresoRepository progresoRepository,
            ApplicationEventPublisher publisher
    ) {
        this.factory = factory;
        this.estrategiaPuntos= estrategiaPuntos;
        this.usuarioRepository = usuarioRepository;
        this.leccionRepository = leccionRepository;
        this.progresoRepository = progresoRepository;
        this.publisher = publisher;
    }
    @Transactional // Garantiza que si falla el guardado, no haya datos inconsistentes
    public FeedbackDTO evaluarDecision(RespuestaEstudianteDTO dto) {
        // 1. REGLA ANTIFRAUDE
        boolean yaAprobo = progresoRepository.existsByUsuarioIdAndLeccionIdAndCompletadoTrue(
                dto.usuarioId(), dto.leccionId());

        if (yaAprobo) {
            throw new LeccionYaCompletadaException(
                    "FRAUDE DETECTADO: El usuario " + dto.usuarioId() + "ya completo con éxito la lección" + dto.leccionId() + ". No se permiten puntos duplicados."
            );
        }

        // 2. EXTRAER DATOS
        Usuario usuario = usuarioRepository.findById(dto.usuarioId()).
                orElseThrow(() -> new RecursoNoEncontradoException("Error: opcion con id " + dto.usuarioId() + " no encontrada"));

        Leccion leccion = leccionRepository.findById(dto.leccionId()).
                orElseThrow(() -> new RecursoNoEncontradoException("Error: opcion con id " + dto.leccionId() + " no encontrada"));

        // 3. ORQUESTAR: El Service pide la herramienta a la Fábrica
            EstrategiaEvaluacion estrategia = factory.obtenerEstrategia(leccion.getTipoEvaluacion());

        // 4. DELEGAR: El Service ejecuta la herramienta de evaluación
        FeedbackDTO feedback = estrategia.evaluar(dto, leccion);

        // 5. PREPARAR EL PROGRESO
        Progreso nuevoProgreso = new Progreso();
        nuevoProgreso.setUsuario(usuario);
        nuevoProgreso.setLeccion(leccion);
        nuevoProgreso.setFechaIntento(LocalDateTime.now());
        nuevoProgreso.setCompletado(feedback.esCorrecto());
        nuevoProgreso.setNivelAlcanzado("Principiante");

        // 6. PROCESAR RESULTADO Y EMITIR EVENTO
        if(feedback.esCorrecto()){
            // Calculamos puntos con nuestra estrategia de gamificación
            int puntosGanados =  estrategiaPuntos.calcularPuntos(nuevoProgreso);
            nuevoProgreso.setPuntajeObtenido(puntosGanados);
            // Actualizamos la entidad Usuario
            usuario.setPuntosExperiencia(usuario.getPuntosExperiencia()+puntosGanados);
            usuarioRepository.save(usuario);

            // 🔥 ¡EL GRITO DEL EVENTO!
            // Como la lección fue un éxito, creamos el hecho inmutable y lo lanzamos al aire
            LeccionCompletadaEvent event = new LeccionCompletadaEvent(
                    usuario.getId(),
                    leccion.getId(),
                    puntosGanados
            );
            publisher.publishEvent(event);
        }else{
            nuevoProgreso.setPuntajeObtenido(0);
        }
        // 7. PERSISTIR PROGRESO Y RETORNAR
        progresoRepository.save(nuevoProgreso);
        return feedback;
    }
}
