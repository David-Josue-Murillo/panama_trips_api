package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthCreateUserRequest(
        @NotBlank String name,
        @NotBlank String lastname,
        @NotBlank String dni,
        @NotBlank String email,
        @NotBlank String password) {
}
