package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReservationRequest(
        @NotNull(message = "El ID de usuario es obligatorio")
        @Positive(message = "El ID de usuario debe ser un número positivo")
        Long userId,

        @NotNull(message = "El ID del tour es obligatorio")
        @Positive(message = "El ID del tour debe ser un número positivo")
        Integer tourPlanId,

        @NotNull(message = "La fecha de reserva es obligatoria")
        LocalDate reservationDate,

        @NotNull(message = "El precio total es obligatorio")
        @DecimalMin(value = "0.00", message = "El precio total no puede ser negativo")
        BigDecimal totalPrice
) { }
