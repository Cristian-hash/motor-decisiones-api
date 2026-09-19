package com.arquitectura.motor_decisiones.controller;

import com.arquitectura.motor_decisiones.repository.ProgresoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
    private final ProgresoRepository progresoRepository;

    public UsuarioController(ProgresoRepository progresoRepository){
        this.progresoRepository = progresoRepository;
    }

    @GetMapping("/{usuarioId}/siguiente-leccion")
    public ResponseEntity<Long> obtenerSiguienteLeccion(@PathVariable Long usuarioId){
        Long ultimaLeccion = progresoRepository.findUltimaLeccionCompletada(usuarioId);
        Long siguienteLeccion = ultimaLeccion + 1;
        return ResponseEntity.ok(siguienteLeccion);
    }
}
