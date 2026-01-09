package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.*;

public record TourTranslationRequest(
        @NotNull(message = "El ID del plan de tour es obligatorio") 
        @Positive(message = "El ID del plan de tour debe ser un número positivo") 
        Integer tourPlanId,

        @NotBlank(message = "El código del idioma es obligatorio") 
        @Pattern(regexp = "^[A-Z]{2}$", message = "El código debe ser de 2 letras mayúsculas (formato ISO 639-1)") 
        String languageCode,

        @Size(max = 150, message = "El título no puede exceder los 150 caracteres") 
        String title,

        String shortDescription,
        String description,
        String includedServices,
        String excludedServices,
        String whatToBring,
        String meetingPoint) {
}
