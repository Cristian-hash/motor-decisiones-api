package com.arquitectura.motor_decisiones.controller;

import com.arquitectura.motor_decisiones.dto.FeedbackDTO;
import com.arquitectura.motor_decisiones.dto.RespuestaEstudianteDTO;
import com.arquitectura.motor_decisiones.service.EvaluacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/evaluaciones")
public class EvaluacionController {
    private EvaluacionService evaluacionService;
    public EvaluacionController(EvaluacionService evaluacionService){
        this.evaluacionService=evaluacionService;
    }

    @PostMapping
    public ResponseEntity<FeedbackDTO> evaluar(@RequestBody RespuestaEstudianteDTO dto){
        FeedbackDTO respuesta= evaluacionService.evaluarDecision(dto);
        return ResponseEntity.ok(respuesta);
    }
}
