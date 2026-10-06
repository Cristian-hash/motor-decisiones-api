package com.arquitectura.motor_decisiones.controller;

import com.arquitectura.motor_decisiones.dto.NivelMapaDTO;
import com.arquitectura.motor_decisiones.service.MapaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mapa")
public class MapaController {
    private final MapaService mapaService;

    public MapaController(MapaService mapaService) {
        this.mapaService = mapaService;
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<NivelMapaDTO>> obtenerRuta(@PathVariable Long usuarioId) {
        List<NivelMapaDTO> ruta = mapaService.obtenerMapaDelUsuario(usuarioId);
        return ResponseEntity.ok(ruta);
    }
}