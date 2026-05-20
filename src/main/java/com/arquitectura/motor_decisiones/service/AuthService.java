package com.arquitectura.motor_decisiones.service;

import com.arquitectura.motor_decisiones.dto.AuthRequestDTO;
import com.arquitectura.motor_decisiones.dto.AuthResponseDTO;
import com.arquitectura.motor_decisiones.entity.Usuario;
import com.arquitectura.motor_decisiones.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private UsuarioRepository usuarioRepository;
    private JwtService jwtService;
    public AuthService(UsuarioRepository usuarioRepository,JwtService jwtService){
        this.usuarioRepository=usuarioRepository;
        this.jwtService=jwtService;
    }

    public AuthResponseDTO login(AuthRequestDTO request){
        // 1. Buscar al usuario por su email
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(()-> new RuntimeException("Credenciales inválidas"));

        // 2. Comparar la contraseña (Por ahora en texto plano, luego integraremos BCrypt)
        if(!usuario.getPassword().equals(request.password())){
            throw new RuntimeException("Credenciales invalidas");
        }
        // 3. Si todo es correcto, generamos el "pasaporte"
        // (Hoy es un string simulado, pronto usaremos la llave maestra del application.properties)
        String tokenReal = jwtService.generateToken(usuario);
        return new AuthResponseDTO(tokenReal);
    }
}
