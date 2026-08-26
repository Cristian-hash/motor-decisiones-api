package com.arquitectura.motor_decisiones.service;

import com.arquitectura.motor_decisiones.dto.AuthRequestDTO;
import com.arquitectura.motor_decisiones.dto.AuthResponseDTO;
import com.arquitectura.motor_decisiones.entity.Usuario;
import com.arquitectura.motor_decisiones.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private UsuarioRepository usuarioRepository;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    public AuthService(UsuarioRepository usuarioRepository, JwtService jwtService, AuthenticationManager authenticationManager){
        this.usuarioRepository=usuarioRepository;
        this.jwtService=jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponseDTO login(AuthRequestDTO request){
        try{
            // 1. Delegamos el trabajo sucio al Jefe de Seguridad
            // Él irá a PostgreSQL y usará BCrypt para saber si "123456" coincide con el Hash guardado.
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(),request.password())
            );


            Usuario usuario = usuarioRepository.findByEmail(request.email())
                    .orElseThrow(()-> new RuntimeException("Credenciales inválidas"));

            // 3. Si todo es correcto, generamos el "pasaporte"
            // (Hoy es un string simulado, pronto usaremos la llave maestra del application.properties)
            String tokenReal = jwtService.generateToken(usuario);
            return new AuthResponseDTO(tokenReal);
        }catch( Exception e ){
            e.printStackTrace();
            System.err.println("ERROR REAL EN LOGIN:"+e.getMessage());
            throw new RuntimeException("",e);
        }
    }
}
