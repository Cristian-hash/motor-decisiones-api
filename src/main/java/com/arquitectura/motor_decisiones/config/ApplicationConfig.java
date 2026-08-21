package com.arquitectura.motor_decisiones.config;

import com.arquitectura.motor_decisiones.repository.UsuarioRepository;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {

    private final UsuarioRepository usuarioRepository;

    public ApplicationConfig(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    //1.EL MAPA: Le enseñamosa Spring como buscar al usuario en PostgreSQL

    @Bean
    public UserDetailsService userDetailsService(){
           return new UserDetailsService() {
               @Override
               public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                    com.arquitectura.motor_decisiones.entity.Usuario usuario = usuarioRepository.findByEmail(username)
                            .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado en la boveda"+username));
                    return User.builder()
                            .username(usuario.getEmail())
                            .password(usuario.getPassword())
                            .roles("ESTUDIANTE")
                            .build();
               }
           };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    //3. EL GERENTE: La herramienta que invoca tu AuthServicepara validar el login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}