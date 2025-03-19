package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.*;

public record ProviderRequest(
        @NotBlank(message = "El RUC es obligatorio")
        @Size(max = 25, message = "El RUC no puede exceder los 25 caracteres")
        @Pattern(regexp = "^[0-9]{1,2}-[0-9]{1,6}-[0-9]{1,6}$", message = "El RUC debe tener un formato válido, ej. 8-123-456789")
        String ruc,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
        @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$", message = "El nombre solo puede contener letras y espacios")
        String name,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "Debe ser un correo electrónico válido")
        @Size(max = 100, message = "El correo no puede exceder los 100 caracteres")
        String email,

        @NotBlank(message = "El teléfono es obligatorio")
        @Size(max = 15, message = "El teléfono no puede exceder los 15 caracteres")
        @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "El teléfono debe ser un número válido de 7 a 15 dígitos, opcionalmente con + al inicio")
        String phone,

        @NotNull(message = "El ID de la provincia es obligatorio")
        @Positive(message = "El ID de la provincia debe ser un número positivo")
        Integer provinceId,

        @NotNull(message = "El ID del distrito es obligatorio")
        @Positive(message = "El ID del distrito debe ser un número positivo")
        Integer districtId,

        @NotNull(message = "El ID de la dirección es obligatorio")
        @Positive(message = "El ID de la dirección debe ser un número positivo")
        Integer addressId
) {}
