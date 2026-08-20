package com.arquitectura.motor_decisiones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
// Cambios en el metodo ApplicationConfig. me decia que
// Using generated security password: 35c81150... Cuando levantaba azure.
// problemas en compilacion con estos 2
// // 2. El Ensamblador: Une la BD con el encriptador
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        // Ahora el compilador sabe con 100% de certeza que userDetailsService() retorna un UserDetailsService
//        authProvider.setUserDetailsService(userDetailsService());
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }
