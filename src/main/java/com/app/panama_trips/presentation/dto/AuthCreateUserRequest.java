package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.*;

public record AuthCreateUserRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 75, message = "The name must be less than 75 characters.")
        @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$", message = "The name must contain only letters.")
        String name,

        @NotBlank(message = "Lastname is required")
        @Size(min = 2, max = 75, message = "The lastname must be less than 75 characters.")
        @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$", message = "The lastname must contain only letters.")
        String lastname,

        @NotBlank(message = "DNI is required")
        @Size(min = 9, max = 13, message = "The DNI must be between 9 and 13 characters.")
        @Pattern(regexp = "^\\d{1,2}-\\d{1,3}-\\d{1,6}$", message = "The DNI must be in the format 00-000-000000.")
        String dni,

        @NotBlank(message = "Email is required")
        @Email(message = "Email is not validated")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 16, message = "The password must be between 6 and 16 characters.")
        String password) {
}
