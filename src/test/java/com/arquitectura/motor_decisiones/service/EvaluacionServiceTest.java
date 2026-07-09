package com.arquitectura.motor_decisiones.service;

import com.arquitectura.motor_decisiones.events.EventPublisher;
import com.arquitectura.motor_decisiones.repository.LeccionRepository;
import com.arquitectura.motor_decisiones.repository.ProgresoRepository;
import com.arquitectura.motor_decisiones.repository.UsuarioRepository;
import com.arquitectura.motor_decisiones.service.gamificacion.CalculadoraPuntosStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
