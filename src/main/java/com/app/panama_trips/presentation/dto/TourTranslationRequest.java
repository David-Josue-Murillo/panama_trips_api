package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.*;

public record TourTranslationRequest(
        @NotNull(message = "El ID del plan de tour es obligatorio") 
        @Positive(message = "El ID del plan de tour debe ser un número positivo") 
        Integer tourPlanId,

        @NotBlank(message = "El código del idioma es obligatorio") 
        @Pattern(regexp = "^[A-Z]{2}$", message = "El código debe ser de 2 letras mayúsculas (formato ISO 639-1)") 
        String languageCode,

        @NotBlank(message = "El título es obligatorio")
        @Size(max = 150, message = "El título no puede exceder los 150 caracteres") 
        String title,

        @Size(max = 500, message = "La descripción corta no puede exceder 500 caracteres")
        String shortDescription,

        @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
        String description,

        @Size(max = 1000, message = "Los servicios incluidos no pueden exceder 1000 caracteres")
        String includedServices,

        @Size(max = 1000, message = "Los servicios excluidos no pueden exceder 1000 caracteres")
        String excludedServices,

        @Size(max = 1000, message = "Qué llevar no puede exceder 1000 caracteres")
        String whatToBring,

        @Size(max = 255, message = "El punto de encuentro no puede exceder 255 caracteres")
        String meetingPoint) {
}
