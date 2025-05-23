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
        ErrorResponse body = response.getBody();
        assertNotNull(body, "Response body should not be null");
        assertEquals("User not found", body.message());
        assertEquals(404, body.status());
    }

    @Test
    void handlerValidationException_shouldReturnBadRequestStatus() {
        // Given
        ValidationException exception = new ValidationException("Invalid input data");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse body = response.getBody();
        assertNotNull(body, "Response body should not be null");
        assertEquals("Invalid input data", body.message());
        assertEquals(400, body.status());
    }

    @Test
    void handlerResourceNotFoundException_shouldReturnNotFoundStatus() {
        // Given
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleResourceNotFoundException(exception);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse body = response.getBody();
        assertNotNull(body, "Response body should not be null");
        assertEquals("Resource not found", body.message());
        assertEquals(404, body.status());
    }

    @Test
    void handleIllegalArgumentException_shouldReturnBadRequestStatus() {
        // Given
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgumentException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse body = response.getBody();
        assertNotNull(body, "Response body should not be null");
        assertEquals("Invalid argument", body.message());
        assertEquals(400, body.status());
    }
}
