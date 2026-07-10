package com.arquitectura.motor_decisiones.service;

import com.arquitectura.motor_decisiones.dto.FeedbackDTO;
import com.arquitectura.motor_decisiones.dto.RespuestaEstudianteDTO;
import com.arquitectura.motor_decisiones.entity.Leccion;
import com.arquitectura.motor_decisiones.entity.Progreso;
import com.arquitectura.motor_decisiones.entity.Usuario;
import com.arquitectura.motor_decisiones.enums.TipoEvaluacion;
import com.arquitectura.motor_decisiones.events.EventPublisher;
import com.arquitectura.motor_decisiones.exception.LeccionYaCompletadaException;
import com.arquitectura.motor_decisiones.repository.LeccionRepository;
import com.arquitectura.motor_decisiones.repository.ProgresoRepository;
import com.arquitectura.motor_decisiones.repository.UsuarioRepository;
import com.arquitectura.motor_decisiones.service.gamificacion.CalculadoraPuntosStrategy;
import com.arquitectura.motor_decisiones.service.strategy.EstrategiaEvaluacion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EvaluacionServiceTest {
    @Mock private ProgresoRepository progresoRepository;
    @Mock private LeccionRepository leccionRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private CalculadoraPuntosStrategy estrategiaPuntos;
    @Mock private EventPublisher eventPublisher;
    @Mock private EvaluacionStrategyFactory factory;

    @InjectMocks
    private EvaluacionService evaluacionService;

    @Test
    void debeRetornarCeroCuandoCountEsInvocadoEnUnMock() {
        long total = progresoRepository.count();
        assertEquals(0,total);
    }
    @Test
    void debeOtorgarPuntosSiRespuestaEsCorrecta(){
        // ----1 .Given(preparar la escena)
        RespuestaEstudianteDTO dto = new RespuestaEstudianteDTO(1L,100L,1L);
        // Fabricamos una lección falsa(la respuesta correcta en nuestro diseño mental)
        Leccion leccionFalsa = new Leccion();
        leccionFalsa.setId(100L);
        leccionFalsa.setTipoEvaluacion(TipoEvaluacion.OPCION_UNICA);

        Usuario usuarioFalso = new Usuario();
        usuarioFalso.setId(1L);
        usuarioFalso.setPuntosExperiencia(0);

        FeedbackDTO feedbackExitoso =  new FeedbackDTO(true,"¡Excelente decisión!",10,"sigue");
        EstrategiaEvaluacion estrategiaFalsa =  mock(EstrategiaEvaluacion.class);
        // 1. Simular que NO hay doble clic (El escudo permite el paso)
        when(progresoRepository.existsByUsuarioIdAndLeccionIdAndCompletadoTrue(1L,100L))
                .thenReturn(false);

        //2. Simular que las entidades existen en la BD
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuarioFalso));

        when(leccionRepository.findById(100L))
                .thenReturn(Optional.of(leccionFalsa));

        when(factory.obtenerEstrategia(TipoEvaluacion.OPCION_UNICA))
                .thenReturn(estrategiaFalsa);
        when(estrategiaFalsa.evaluar(dto,leccionFalsa)).
                thenReturn(feedbackExitoso);

        FeedbackDTO resultado = evaluacionService.evaluarDecision(dto);

        assertTrue(resultado.esCorrecto(),"El resultado debe ser correcto");

        verify(progresoRepository, times(1)).save(any(Progreso.class));

    }

    @Test
    void lanzarExcepcionSiLeccionYaEstaCompletada(){
        //1
        RespuestaEstudianteDTO dto = new RespuestaEstudianteDTO(1L,100L,1L);
        //2-
        when(progresoRepository.existsByUsuarioIdAndLeccionIdAndCompletadoTrue(1L,100L))
                .thenReturn(true);

        assertThrows(LeccionYaCompletadaException.class,()->{
            evaluacionService.evaluarDecision(dto);
        });

        verify(progresoRepository,never()).save(any(Progreso.class));

    }
}
