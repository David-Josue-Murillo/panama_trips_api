package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.presentation.dto.AuthCreateUserRequest;
import com.app.panama_trips.presentation.dto.AuthLoginRequest;
import com.app.panama_trips.presentation.dto.AuthResponse;
import com.app.panama_trips.service.implementation.UserAuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    UserAuthService userAuthService;

    @InjectMocks
    AuthController authController;

    @Test
    void register_shouldReturnCreatedStatus() {
        // Given
        AuthCreateUserRequest authCreateUserRequest = DataProvider.userAuthCreateUserRequestMock();
        AuthResponse authResponseUser = DataProvider.userAuthResponseMock();
        when(userAuthService.create(authCreateUserRequest)).thenReturn(authResponseUser);

        // When
        ResponseEntity<AuthResponse> response = authController.register(authCreateUserRequest);

        // Then
        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(authResponseUser, response.getBody());
    }

    @Test
    void register_shouldReturnConflict_whenUserAlreadyExists() {
        // Given
        AuthCreateUserRequest authCreateUserRequest = DataProvider.userAuthCreateUserRequestMock();
        when(userAuthService.create(authCreateUserRequest)).thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "User already exists"));

        // When
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authController.register(authCreateUserRequest);
        });

        // Then
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("User already exists", exception.getReason());
    }


    @Test
    void login_shouldReturnOkStatus() {
        // Given
        AuthLoginRequest authLoginRequest = DataProvider.userAuthLoginRequestMock();
        AuthResponse authResponseUser = DataProvider.userAuthResponseMock();
        when(userAuthService.login(authLoginRequest)).thenReturn(authResponseUser);

        // When
        ResponseEntity<AuthResponse> response = authController.login(authLoginRequest);

        // Then
        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponseUser, response.getBody());
    }

    @Test
    void login_shouldReturnUnauthorized_whenCredentialsAreInvalid() {
        // Given
        AuthLoginRequest authLoginRequest = DataProvider.userAuthLoginRequestMock();
        when(userAuthService.login(authLoginRequest)).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        // When
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authController.login(authLoginRequest);
        });

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Invalid credentials", exception.getReason());

    }
}
