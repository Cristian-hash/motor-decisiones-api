package com.arquitectura.motor_decisiones.exception;

import com.arquitectura.motor_decisiones.dto.ErrorResponseDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoAusenteException.class)
    public ResponseEntity<ErrorResponseDTO> manejarRecursoNoEncontrado(RecursoAusenteException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // 2. NUEVO ESCUDO: Atrapa el Candado de Titanio (Unique Constraint SQL)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> manejarDataIntegrityViolation(DataIntegrityViolationException ex) {
        // Traducimos la explosión SQL a un mensaje educado para el usuario
        ErrorResponseDTO error = new ErrorResponseDTO(
                "Estamos procesando tu solicitud. Por favor, no hagas doble clic.",
                HttpStatus.CONFLICT.value(), // 409
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    // 3. NUEVO ESCUDO: Atrapa el Bloqueo Optimista (@Version)
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponseDTO> manejarOptimisticLocking(ObjectOptimisticLockingFailureException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                "Conflicto de actualización. Alguien más modificó este dato. Por favor, recarga la página.",
                HttpStatus.CONFLICT.value(), // 409
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
