package com.arquitectura.motor_decisiones.config;

import com.arquitectura.motor_decisiones.entity.Leccion;
import com.arquitectura.motor_decisiones.entity.OpcionRespuesta;
import com.arquitectura.motor_decisiones.entity.Patron;
import com.arquitectura.motor_decisiones.repository.LeccionRepository;
import com.arquitectura.motor_decisiones.repository.OpcionRespuestaRepository;
import com.arquitectura.motor_decisiones.repository.PatronRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner initData(
            PatronRepository patronRepository,
            LeccionRepository leccionRepository,
            OpcionRespuestaRepository opcionRespuestaRepository){
        return args ->{
            // Regla de oro: Solo inyectar si la base de datos está vacía
        if(patronRepository.count()>0){
            return;
        }
        System.out.println("Sembrando la Base de Datos con el Patrón Strategy...");

        //1. Construir el Patrón
            Patron strategy=new Patron();
            strategy.setNombre("Strategy");
            strategy.setTipo("Patrones de Comportamiento");
            strategy.setDescripcionCorta("Permite intercambiar algoritmos (comportamientos) en tiempo de ejecución sin alterar el código cliente.");
            patronRepository.save(strategy);
        //2. Construir la leccionn
            Leccion leccion= new Leccion();
            leccion.setTitulo("Cómo eliminar los Ifs gigante");
            leccion.setPatron(strategy);

            // Usamos bloques de texto de Java (comillas triples) para mantener el formato visual aquí
            leccion.setProblemaHook("""
            **El Problema:**
            Tienes un sistema de pagos donde el código se ve así:
            if (tipo == "VISA") { ... } else if (tipo == "PAYPAL") { ... }`
            
            Cada vez que agregas un método nuevo, el archivo crece y el riesgo de errores aumenta exponencialmente.
            """);
            leccion.setMetafora("""
            Imagina un **Gimnasio**:
            * La **Estrategia** es tu rutina (Cardio, Pesas, Yoga).
            * El **Contexto** eres tú.
            * Tú decides qué rutina aplicar hoy sin cambiar quién eres.
            """);

            leccion.setPseudocodigo("""
            ```text
            1. Definir Interfaz Estrategia
            2. Crear Clase PagoTarjeta implementa Estrategia
            3. Crear Clase PagoEfectivo implementa Estrategia
            4. El Cliente usa la interfaz, no la clase concreta
            ```
            """);

            leccion.setCodigoJava("""
            ```java
            public interface EstrategiaPago {
            void procesar(int monto);
            }
    
            public class PagoTarjeta implements EstrategiaPago {
                @Override
                public void procesar(int monto) {
                    System.out.println("Cobrando " + monto + " a la tarjeta.");
                }
            }
            ```
            """);

            leccion.setPuntosRecompensa(100);
            leccionRepository.save(leccion);

            // 3. Construir las Opciones (1 correcta, 2 incorrectas)

            OpcionRespuesta op1 = new OpcionRespuesta();
            op1.setLeccion(leccion);
            op1.setTextoOpcion("A) Usar una estructura switch-case gigante en la clase principal.");
            op1.setEsCorrecta(false);
            op1.setJustificacionFeedback("Incorrecto. Si agregas un nuevo método, tendrás que modificar el código central, violando el principio Open/Closed.");
            opcionRespuestaRepository.save(op1);

            OpcionRespuesta op2 = new OpcionRespuesta();
            op2.setLeccion(leccion);
            op2.setTextoOpcion("B) Crear una interfaz 'EnvioStrategy' y una clase para cada tipo.");
            op2.setEsCorrecta(true);
            op2.setJustificacionFeedback("¡Correcto! Delegas la lógica a clases independientes. Si necesitas algo nuevo, simplemente creas una nueva clase que implemente la interfaz.");
            opcionRespuestaRepository.save(op2);

            OpcionRespuesta op3 = new OpcionRespuesta();
            op3.setLeccion(leccion);
            op3.setTextoOpcion("C) Crear el patrón Singleton para asegurar una única instancia.");
            op3.setEsCorrecta(false);
            op3.setJustificacionFeedback("Incorrecto. Singleton restringe la creación de objetos a uno solo, no te ayuda a intercambiar comportamientos.");
            opcionRespuestaRepository.save(op3);

            System.out.println("¡Datos sembrados con éxito!");
        };
    }
}
