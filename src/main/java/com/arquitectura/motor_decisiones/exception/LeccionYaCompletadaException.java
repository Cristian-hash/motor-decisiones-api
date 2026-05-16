package com.arquitectura.motor_decisiones.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LeccionYaCompletadaException extends RuntimeException {
    public LeccionYaCompletadaException(String message) {
        super(message);
    }
}
