package com.arquitectura.motor_decisiones.service;

import com.arquitectura.motor_decisiones.dto.FeedbackDTO;
import com.arquitectura.motor_decisiones.dto.RespuestaEstudianteDTO;

import com.arquitectura.motor_decisiones.entity.Leccion;
import com.arquitectura.motor_decisiones.entity.OpcionRespuesta;
import com.arquitectura.motor_decisiones.entity.Progreso;
import com.arquitectura.motor_decisiones.entity.Usuario;
import com.arquitectura.motor_decisiones.enums.TipoEvaluacion;
import com.arquitectura.motor_decisiones.exception.LeccionYaCompletadaException;
import com.arquitectura.motor_decisiones.exception.RecursoNoEncontradoException;
import com.arquitectura.motor_decisiones.repository.LeccionRepository;
import com.arquitectura.motor_decisiones.repository.OpcionRespuestaRepository;
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
    private final Map<TipoEvaluacion, EstrategiaEvaluacion> estrategias;

    //1RO TRAERME EL REPOSITORY
    // private final OpcionRespuestaRepository opcionRepository;
    private final UsuarioRepository usuarioRepository;
    private final LeccionRepository leccionRepository;
    private final ProgresoRepository progresoRepository;

    @Autowired
    public EvaluacionService(
        List<EstrategiaEvaluacion> estrategiaList,
        UsuarioRepository usuarioRepository,
        LeccionRepository leccionRepository,
        ProgresoRepository progresoRepository
    ){
        this.usuarioRepository = usuarioRepository;
        this.leccionRepository = leccionRepository;
        this.progresoRepository = progresoRepository;

        this.estrategias= estrategiaList.stream()
                .collect(Collectors.toMap(
                        estrategia -> estrategia.getTipo(),
                        estrategia -> estrategia
                ));
    }

    @Transactional // Garantiza que si falla el guardado, no haya datos inconsistentes
    public FeedbackDTO evaluarDecision(RespuestaEstudianteDTO dto){
        //REGLA ANTIFRAUDE
        boolean yaAprobo = progresoRepository.existByUsuarioByIdAndLeccionIdAndCompletadoTrue(
                dto.usuarioId(),dto.leccionId());

        if(yaAprobo){
            throw new LeccionYaCompletadaException(
                    "FRAUDE DETECTADO: El usuario "+dto.usuarioId()+"ya completo con éxito la lección"+dto.leccionId()+ ". No se permiten puntos duplicados."
            );
        }

        // 1. Extraer los datos básicos
        Usuario usuario = usuarioRepository.findById(dto.usuarioId()).
                orElseThrow(()->new RecursoNoEncontradoException("Error: opcion con id "+dto.usuarioId()+" no encontrada"));

        Leccion leccion = leccionRepository.findById(dto.leccionId()).
                orElseThrow(()-> new RecursoNoEncontradoException("Error: opcion con id "+dto.leccionId()+" no encontrada"));

        // 2. ORQUESTAR: Buscar al especialista en la agenda según el tipo de lección
        EstrategiaEvaluacion estrategia = estrategias.get(leccion.getTipoEvaluacion());

        // 3. DELEGAR LA LÓGICA (La estrategia se encarga de buscar la OpcionRespuesta y evaluarla)
        FeedbackDTO feedback= estrategia.evaluar(dto,leccion);

        // 4. GUARDAR EL PROGRESO (Usamos el resultado que nos devolvió el especialista)
        Progreso nuevoProgreso=new Progreso();
        nuevoProgreso.setUsuario(usuario);
        nuevoProgreso.setLeccion(leccion);
        nuevoProgreso.setFechaIntento(LocalDateTime.now());
        nuevoProgreso.setCompletado(feedback.esCorrecto());
        nuevoProgreso.setPuntajeObtenido(feedback.esCorrecto()?leccion.getPuntosRecompensa():0);
        nuevoProgreso.setNivelAlcanzado("Principiante");
        //5. se guarda el progreso
        progresoRepository.save(nuevoProgreso);
        // 6. Retornar al Front
        return feedback;
    }
}
