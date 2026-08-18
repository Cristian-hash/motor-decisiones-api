package com.arquitectura.motor_decisiones.consumers;

import com.arquitectura.motor_decisiones.events.LeccionCompletadaEvent;
import com.arquitectura.motor_decisiones.service.gamificacion.LogrosService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class GamificacionKafkaConsumer{
    private final LogrosService logrosService;
    private final ObjectMapper objectMapper;

    public GamificacionKafkaConsumer(LogrosService logrosService,ObjectMapper objectMapper){
                this.logrosService=logrosService;
                this.objectMapper=objectMapper;
    }
    // APAGADO TEMPORAL PARA LA NUBE: Evitamos que Spring cree el hilo que busca el puerto 9092
    //@KafkaListener(topics = "gamificacion-topic",groupId = "tesis-group")
    public void escucharEventoGamificacion(String mensajeJson){
        System.out.println("=========================================");
        System.out.println("📥 [KAFKA CONSUMER] Mensaje atrapado desde la nube:");
        System.out.println(mensajeJson);

        try{
            // 1. Traducimos el String JSON de vuelta a nuestro Récord Java
            LeccionCompletadaEvent evento = objectMapper.readValue(mensajeJson, LeccionCompletadaEvent.class);
            // 2. Delegamos la responsabilidad al especialista (LogrosService)
            System.out.println(" Procesando logros para el usuario ID: " + evento.usuarioId());
            logrosService.evaluarInsignias(evento.usuarioId());
        }catch(Exception e){
            System.err.println("❌ Error al procesar el mensaje de Kafka: " + e.getMessage());
        }
        System.out.println("=========================================");
    }
}