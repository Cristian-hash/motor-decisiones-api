package com.arquitectura.motor_decisiones.controller;

import com.arquitectura.motor_decisiones.dto.LeccionCompletaDTO;
import com.arquitectura.motor_decisiones.service.LeccionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/lecciones")
@CrossOrigin(origins = "*")
public class LeccionController {
    private final LeccionService leccionService;
    public LeccionController(LeccionService leccionService){
        this.leccionService=leccionService;
    }

    @GetMapping("/{id}")
    public LeccionCompletaDTO getLeccion(@PathVariable Long id){
        return leccionService.obtenerLeccionPorId(id);
    }
}
