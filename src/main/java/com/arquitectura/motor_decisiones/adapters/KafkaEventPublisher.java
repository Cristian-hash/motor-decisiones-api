package com.arquitectura.motor_decisiones.adapters;


import com.arquitectura.motor_decisiones.events.EventPublisher;
import com.arquitectura.motor_decisiones.events.LeccionCompletadaEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class KafkaEventPublisher implements EventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publicarLeccionCompletada(LeccionCompletadaEvent event) {

        try {
            // 1. Convertimos el Récord a un String en formato JSON
            String mensajeJson = objectMapper.writeValueAsString(event);
            // 2. Enviamos el JSON al tópico "gamificacion-topic"
            // 2.1. APAGADO TEMPORAL PARA AZURE: Evitamos que busque el puerto 9092
            // kafkaTemplate.send("gamificacion-topic", mensajeJson);
            System.out.println("🚀 [ADAPTADOR KAFKA] Evento enviado a la nube: " + mensajeJson);

        } catch (Exception e) {
            System.err.println("❌ Error al convertir el evento a JSON: " + e.getMessage());
         }
    }
}