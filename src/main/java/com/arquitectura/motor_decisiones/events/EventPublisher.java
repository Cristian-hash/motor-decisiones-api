package com.arquitectura.motor_decisiones.events;

public interface EventPublisher {
    void publicarLeccionCompletada(LeccionCompletadaEvent event);
}