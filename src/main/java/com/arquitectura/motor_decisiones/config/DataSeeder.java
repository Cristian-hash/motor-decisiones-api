package com.arquitectura.motor_decisiones.config;

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
            OpcionRespuestaRepository opcionRespuestaRepository
    ){
        return args ->{
            // Regla de oro: Solo inyectar si la base de datos está vacía
        if(patronRepository.count()>0){
            return;
        }
        System.out.println("Sembrando la Base de Datos con el Patrón Strategy...");

        }
    }
}
