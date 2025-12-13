package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LanguageRequest(
    @NotBlank(message = "El código del idioma es obligatorio") 
    @Pattern(regexp = "^[A-Z]{2}$", message = "El código debe ser de 2 letras mayúsculas (formato ISO 639-1)") 
    String code,

    @NotBlank(message = "El nombre del idioma es obligatorio") 
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres") 
    String name,

    Boolean isActive
) {}
