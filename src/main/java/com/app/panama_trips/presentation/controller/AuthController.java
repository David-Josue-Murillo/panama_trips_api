package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.AuthCreateUserRequest;
import com.app.panama_trips.presentation.dto.AuthLoginRequest;
import com.app.panama_trips.presentation.dto.AuthResponse;
import com.app.panama_trips.service.implementation.UserAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for authentication")
public class AuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/signup")
    @Operation(
            summary = "Register a new user",
            description = "Register a new user in the system",
            tags = {"Authentication", "User"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User data to register",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = AuthCreateUserRequest.class)
                    )
            ),
            responses =  @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "User created",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = AuthResponse.class)
                    )
            )
    )
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthCreateUserRequest authCreateUserRequest) {
        return new ResponseEntity<>(this.userAuthService.create(authCreateUserRequest), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Login a user",
            description = "Login a user in the system",
            tags = {"Authentication", "User"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User data to login",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = AuthLoginRequest.class)
                    )
            ),
            responses =  @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User logged in",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = AuthResponse.class)
                    )
            )
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest authLoginRequest) {
        return new ResponseEntity<>(this.userAuthService.login(authLoginRequest), HttpStatus.OK);
    }
}
