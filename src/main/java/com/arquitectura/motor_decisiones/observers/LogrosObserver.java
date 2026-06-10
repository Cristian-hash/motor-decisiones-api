package com.arquitectura.motor_decisiones.observers;

import com.arquitectura.motor_decisiones.events.LeccionCompletadaEvent;
import com.arquitectura.motor_decisiones.service.gamificacion.LogrosService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LogrosObserver {

    private final LogrosService logrosService;

    public LogrosObserver(LogrosService logrosService){
        this.logrosService=logrosService;
    }

    @EventListener
    public void onLeccionCompletada(LeccionCompletadaEvent event){

        // Simulamos la consecuencia en la terminal de desarrollo
        System.out.println("=========================================");
        System.out.println("🎉 [LOGROS OBSERVER] ¡Evento escuchado con éxito!");
        logrosService.evaluarInsignias(event.usuarioId());
        System.out.println("=========================================");
    }
}
