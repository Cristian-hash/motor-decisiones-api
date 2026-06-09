package com.arquitectura.motor_decisiones.observers;

import com.arquitectura.motor_decisiones.events.LeccionCompletadaEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LogrosObserver {
    @EventListener
    public void onLeccionCompletada(LeccionCompletadaEvent event){

        // Simulamos la consecuencia en la terminal de desarrollo
        System.out.println("=========================================");
        System.out.println("🎉 [LOGROS OBSERVER] ¡Evento escuchado con éxito!");
        System.out.println("Otorgando logro al usuario ID: " + event.usuarioId());
        System.out.println("Puntos procesados en gamificación: " + event.puntosGanados());
        System.out.println("=========================================");
    }
}
