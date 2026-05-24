package com.arquitectura.motor_decisiones.controller;

import com.arquitectura.motor_decisiones.dto.AuthRequestDTO;
import com.arquitectura.motor_decisiones.dto.AuthResponseDTO;
import com.arquitectura.motor_decisiones.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthControlller {
    private AuthService authService;
    public AuthControlller(AuthService authService){
        this.authService=authService;
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO>login(@RequestBody AuthRequestDTO request){
        AuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);

    }
}
