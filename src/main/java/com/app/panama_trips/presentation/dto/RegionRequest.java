package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.*;

public record RegionRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
        String name,

        @Positive(message = "El ID de la provincia debe ser un número positivo")
        Integer provinceId,

        @Positive(message = "El ID de la comarca debe ser un número positivo")
        Integer comarcaId
) { }