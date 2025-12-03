package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.*;

public record ReviewCategoryRatingRequest(
    @NotNull(message = "El ID de la reseña es obligatorio") 
    @Positive(message = "El ID de la reseña debe ser un número positivo") 
    Integer reviewId,

    @NotNull(message = "El ID de la categoría es obligatorio") 
    @Positive(message = "El ID de la categoría debe ser un número positivo") 
    Integer categoryId,

    @NotNull(message = "La calificación es obligatoria") 
    @Min(value = 1, message = "La calificación debe ser al menos 1") 
    @Max(value = 5, message = "La calificación no puede exceder 5") 
    Integer rating
) {}
