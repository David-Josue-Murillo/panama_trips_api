package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TourPlanRequest(
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 150, message = "El título no puede exceder los 150 caracteres")
        String title,

        @NotBlank(message = "La descripción es obligatoria")
        String description,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.00", message = "El precio no puede ser negativo")
        BigDecimal price,

        @NotNull(message = "La duración es obligatoria")
        @Positive(message = "La duración debe ser un número positivo")
        Integer duration,

        @NotNull(message = "La cantidad de cupos disponibles es obligatoria")
        @PositiveOrZero(message = "Los cupos disponibles deben ser un número positivo o cero")
        Integer availableSpots,

        @NotNull(message = "El ID del proveedor es obligatorio")
        @Positive(message = "El ID del proveedor debe ser un número positivo")
        Integer providerId
) { }
