package com.arquitectura.motor_decisiones.exception;

import com.arquitectura.motor_decisiones.dto.ErrorResponseDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.TransactionSystemException;
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
    // EL NUEVO ESCUDO INTELIGENTE: Atrapa violaciones directas O envueltas en transacciones
    @ExceptionHandler({DataIntegrityViolationException.class, TransactionSystemException.class})
    public ResponseEntity<ErrorResponseDTO> manejarErroresDeIntegridadYTransaccion(Exception ex) {
        // Verificamos si el error gigante tiene la violación de integridad escondida adentro
        Throwable causaRaiz = ex.getCause();
        if (causaRaiz instanceof DataIntegrityViolationException || ex instanceof DataIntegrityViolationException) {
            ErrorResponseDTO error = new ErrorResponseDTO(
                    "Estamos procesando tu solicitud. Por favor, no hagas doble clic.",
                    HttpStatus.CONFLICT.value(), // 409
                    LocalDateTime.now()
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        // Si es otro tipo de error transaccional extraño, devolvemos un 500 para investigarlo
        ErrorResponseDTO errorGrave = new ErrorResponseDTO(
                "Ocurrió un error inesperado al guardar los datos.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(), // 500
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorGrave);
    }
    //Dom 28 de marzo - comprension de como funcionan las excepciones con la cabecera @RestControllerService
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

    // 4. NUEVO ESCUDO: Atrapa tu regla de negocio (El if antifraude)
    @ExceptionHandler(LeccionYaCompletadaException.class)
    public ResponseEntity<ErrorResponseDTO> manejarLeccionYaCompletada(LeccionYaCompletadaException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                ex.getMessage(), // Usa tu mensaje "FRAUDE DETECTADO..."
                HttpStatus.CONFLICT.value(), // 409
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
