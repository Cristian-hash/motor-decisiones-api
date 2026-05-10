package com.arquitectura.motor_decisiones.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecursoAusenteException extends RuntimeException {
    public RecursoAusenteException(String message) {
        super(message);
    }
}
