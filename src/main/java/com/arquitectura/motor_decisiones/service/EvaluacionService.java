package com.arquitectura.motor_decisiones.service;

import com.arquitectura.motor_decisiones.dto.FeedbackDTO;
import com.arquitectura.motor_decisiones.dto.RespuestaEstudianteDTO;
import com.arquitectura.motor_decisiones.entity.Leccion;
import com.arquitectura.motor_decisiones.entity.Progreso;
import com.arquitectura.motor_decisiones.entity.Usuario;
import com.arquitectura.motor_decisiones.events.EventPublisher;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EvaluacionService {
    //1 Agenda de especialistas
    private final EvaluacionStrategyFactory factory;
    public final CalculadoraPuntosStrategy estrategiaPuntos;
    private final UsuarioRepository usuarioRepository;
    private final LeccionRepository leccionRepository;
    private final ProgresoRepository progresoRepository;

    // El Enchufe (La Interfaz). El servicio no sabe nada de Kafka.
    private final EventPublisher eventPublisher;

    @Autowired
    public EvaluacionService(
            EvaluacionStrategyFactory factory,
            // Inyectamos por defecto la estrategia normal usando @Qualifier si se tiene varias
            @Qualifier("puntosNormalStrategy") CalculadoraPuntosStrategy estrategiaPuntos,
            UsuarioRepository usuarioRepository,
            LeccionRepository leccionRepository,
            ProgresoRepository progresoRepository,
            EventPublisher eventPublisher
    ) {
        this.factory = factory;
        this.estrategiaPuntos= estrategiaPuntos;
        this.usuarioRepository = usuarioRepository;
        this.leccionRepository = leccionRepository;
        this.progresoRepository = progresoRepository;
        this.eventPublisher = eventPublisher;
    }
    @Transactional // Garantiza que si falla el guardado, no haya datos inconsistentes
    public FeedbackDTO evaluarDecision(RespuestaEstudianteDTO dto) {
        // 1. REGLA ANTIFRAUDE (Tu lógica aquí está perfecta)
        boolean yaAprobo = progresoRepository.existsByUsuarioIdAndLeccionIdAndCompletadoTrue(
                dto.usuarioId(), dto.leccionId());
        // --- INICIO DE SIMULACIÓN DE LENTITUD ---
        try {
            System.out.println("Hilo " + Thread.currentThread().getName() + " validó que yaAprobo es: " + yaAprobo);
            Thread.sleep(3000); // Pausamos por 3 segundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // --- FIN DE SIMULACIÓN ---
        if (yaAprobo) {
            throw new LeccionYaCompletadaException(
                    "FRAUDE DETECTADO: El usuario " + dto.usuarioId() + "ya completo con éxito la lección" + dto.leccionId() + ". No se permiten puntos duplicados."
            );
        }

        // 2. EXTRAER DATOS
        Usuario usuario = usuarioRepository.findByIdForUpdate(dto.usuarioId()).
                orElseThrow(() -> new RecursoNoEncontradoException("Error: opcion con id " + dto.usuarioId() + " no encontrada"));

        Leccion leccion = leccionRepository.findById(dto.leccionId()).
                orElseThrow(() -> new RecursoNoEncontradoException("Error: opcion con id " + dto.leccionId() + " no encontrada"));

        // 3. ORQUESTAR: El Service pide la herramienta a la Fábrica
            EstrategiaEvaluacion estrategia = factory.obtenerEstrategia(leccion.getTipoEvaluacion());

        // 4. DELEGAR: El Service ejecuta la herramienta de evaluación
        FeedbackDTO feedback = estrategia.evaluar(dto, leccion);

        // 5. CALCULAR PUNTOS ANTES DE CREAR EL EVENTO
        int puntosGanados = 0;
        if(feedback.esCorrecto()) {
            // Calculamos puntos con nuestra estrategia de gamificación
            puntosGanados = estrategiaPuntos.calcularPuntos(usuario, leccion);

            // Actualizamos la entidad Usuario (Esto actúa como una caché rápida del total)
            usuario.setPuntosExperiencia(usuario.getPuntosExperiencia() + puntosGanados);
            usuarioRepository.save(usuario);
        }

// 6. PREPARAR Y PERSISTIR EL PROGRESO INMUTABLE (El Evento)
        // Usamos el nuevo constructor sin setters. ¡Nace y se queda así para siempre!
        Progreso nuevoProgreso = new Progreso(
                puntosGanados,
                "Principiante",
                usuario,
                leccion,
                feedback.esCorrecto()
        );
        progresoRepository.save(nuevoProgreso);

        // 7. EMITIR EVENTO A KAFKA / OTROS SISTEMAS
        if(feedback.esCorrecto()){
            LeccionCompletadaEvent event = new LeccionCompletadaEvent(
                    usuario.getId(),
                    leccion.getId(),
                    puntosGanados
            );
            eventPublisher.publicarLeccionCompletada(event);
        }
        return feedback;
    }
}