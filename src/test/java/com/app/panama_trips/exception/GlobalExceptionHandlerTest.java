package com.app.panama_trips.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handlerUseNotFoundException_WhenUserNotFoundException() {
        // Given
        UserNotFoundException exception = new UserNotFoundException("User not found");

        // When
        ResponseEntity<Object> response = globalExceptionHandler.handlerUseNotFoundException(exception);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("User not found", body.get("message"));
        assertEquals(404, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handlerValidationException_shouldReturnBadRequestStatus() {
        // Given
        ValidationException exception = new ValidationException("Invalid input data");

        // When
        ResponseEntity<Object> response = globalExceptionHandler.handlerValidationException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Invalid input data", body.get("message"));
        assertEquals(400, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handlerResourceNotFoundException_shouldReturnNotFoundStatus() {
        // Given
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");

        // When
        ResponseEntity<Object> response = globalExceptionHandler.handlerResourceNotFoundException(exception);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Resource not found", body.get("message"));
        assertEquals(404, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleIllegalArgumentException_shouldReturnBadRequestStatus() {
        // Given
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        // When
        ResponseEntity<Object> response = globalExceptionHandler.handleIllegalArgumentException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Invalid argument", body.get("message"));
        assertEquals(400, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }
}
