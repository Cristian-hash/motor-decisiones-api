package com.arquitectura.motor_decisiones.service.gamificacion;

import com.arquitectura.motor_decisiones.entity.Usuario;
import com.arquitectura.motor_decisiones.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class LogrosService {
    private final UsuarioRepository usuarioRepository;

    public LogrosService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void evaluarInsignias(Long usuarioId){
        Usuario usuario = usuarioRepository.findById(usuarioId).
                orElseThrow(()->new RuntimeException("Usuario no encontrado"));

        // 2. Lógica de negocio de Gamificación
        if(usuario.getPuntosExperiencia()>=50){
            // Verificamos que no tenga ya la insignia para no sobreescribir por gusto
            if(usuario.getInsignia()==null || !usuario.getInsignia().equals("Arquitecto Junior")){
                usuario.setInsignia("Arquitecto Junior");
                usuarioRepository.save(usuario);
                System.out.println("🏆 ¡NUEVA INSIGNIA OTORGADA EN BD!");
                System.out.println("El usuario"+usuarioId+"ahora es: Arquitecto Junior");
            }
        }
    }
}