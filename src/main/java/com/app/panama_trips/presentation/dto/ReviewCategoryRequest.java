package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReviewCategoryRequest(
    @NotBlank(message = "Name is required") 
    @Size(max = 50, message = "Name must not exceed 50 characters") 
    String name,

    String description) {
}
