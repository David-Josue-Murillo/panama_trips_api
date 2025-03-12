package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "DNI is required")
        @Size(min = 9, max = 13, message = "DNI must be between 9 and 13 characters")
        String dni,

        @NotBlank(message = "Name is required")
        @Size(max = 75, message = "Name must not exceed 75 characters")
        String name,

        @NotBlank(message = "Lastname is required")
        @Size(max = 75, message = "Lastname must not exceed 75 characters")
        String lastname,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 150, message = "Email must not exceed 150 characters")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        String profileImageUrl
    )
{}
