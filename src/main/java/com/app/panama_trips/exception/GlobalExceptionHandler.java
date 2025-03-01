package com.app.panama_trips.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handlerUseNotFoundException(UserNotFoundException ex) {
        Map<String, Object> body = Map.of(
                "message", ex.getMessage(),
                "status", HttpStatus.NOT_FOUND.value(),
                "timestamp", System.currentTimeMillis()
        );

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handlerValidationException(ValidationException ex) {
        Map<String, Object> body = Map.of(
                "message", ex.getMessage(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "timestamp", System.currentTimeMillis()
        );

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handlerResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> body = Map.of(
                "message", ex.getMessage(),
                "status", HttpStatus.NOT_FOUND.value(),
                "timestamp", System.currentTimeMillis()
        );

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
