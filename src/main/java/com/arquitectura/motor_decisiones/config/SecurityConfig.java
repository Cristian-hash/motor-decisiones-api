package com.arquitectura.motor_decisiones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;


    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter,AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider =authenticationProvider;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                //Le decimos que deje pasar alos que vengan de Angular.
                .cors(cors ->cors.configurationSource(corsConfigurationSource()))
                // 1. Desactivamos CSRF (Cross-Site Request Forgery)
                // ¿Por qué? Porque no usamos cookies de sesión propensas a hackeos de navegador;
                // nuestro frontend (Angular) mandará el JWT de forma explícita en las cabeceras.
                .csrf(AbstractHttpConfigurer::disable)
                // 2. Trazamos las reglas de los pasillos (Rutas públicas vs privadas)
                .authorizeHttpRequests(auth -> auth
                        // La ruta del recepcionista (Login/Registro) es completamente pública
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // Cualquier otra habitación del sistema exige tener el gafete de autenticado
                        .anyRequest().authenticated()
                )
                // 3. Le inyectamos AMNESIA TOTAL al servidor (STATELESS)
                // El servidor no guardará nada en memoria. No hay cuadernos de notas de sesión.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 4. Contratamos al equipo tactico. Le decimos a Spring que use nuestra BD.
                .authenticationProvider(authenticationProvider)
                // 4. Mandamos al Guardia a su puesto de trabajo
                // Ponemos nuestro JwtAuthenticationFilter JUSTO ANTES del filtro estándar de Spring
                // (UsernamePasswordAuthenticationFilter) para que intercepte y valide el token primero.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "https://gentle-wave-0b3057610.7.azurestaticapps.net"
                ));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }
}
