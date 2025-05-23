package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public record GuideRequest(
        @NotNull(message = "El ID del proveedor es requerido")
        Integer providerId,

        @Size(max = 2000, message = "La biografía no puede exceder los 2000 caracteres")
        String bio,

        @NotEmpty(message = "Debe especificar al menos una especialidad")
        @Size(max = 10, message = "No puede exceder las 10 especialidades")
        List<String> specialties,

        @NotEmpty(message = "Debe especificar al menos un idioma")
        @Size(max = 10, message = "No puede exceder los 10 idiomas")
        List<String> languages,

        @NotNull(message = "Los años de experiencia son requeridos")
        @Min(value = 0, message = "Los años de experiencia deben ser positivos")
        @Max(value = 100, message = "Los años de experiencia no pueden exceder 100")
        Integer yearsExperience,

        @Size(max = 2000, message = "Los detalles de certificación no pueden exceder los 2000 caracteres")
        String certificationDetails,

        @NotNull(message = "El estado activo es requerido")
        Boolean isActive
) {}
