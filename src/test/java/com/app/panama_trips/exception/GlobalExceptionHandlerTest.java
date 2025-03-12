package com.app.panama_trips.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void handlerUseNotFoundException_WhenUserNotFoundException() {
        // Given
        UserNotFoundException exception = new UserNotFoundException("User not found");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerUseNotFoundException(exception);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals("User not found", response.getBody().message());
        assertEquals(404, response.getBody().status());
    }

    @Test
    void handlerValidationException_shouldReturnBadRequestStatus() {
        // Given
        ValidationException exception = new ValidationException("Invalid input data");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals("Invalid input data", response.getBody().message());
        assertEquals(400, response.getBody().status());
    }

    @Test
    void handlerResourceNotFoundException_shouldReturnNotFoundStatus() {
        // Given
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleResourceNotFoundException(exception);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals("Resource not found", response.getBody().message());
        assertEquals(404, response.getBody().status());
    }

    @Test
    void handleIllegalArgumentException_shouldReturnBadRequestStatus() {
        // Given
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgumentException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals("Invalid argument", response.getBody().message());
        assertEquals(400, response.getBody().status());
    }
}
