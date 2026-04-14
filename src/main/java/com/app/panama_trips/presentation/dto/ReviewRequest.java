package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * DTO para crear o actualizar un review.
 *
 * @param userId           ID del usuario que escribe el review
 * @param tourPlanId       ID del tour plan evaluado
 * @param title            Título del review (máximo 100 caracteres)
 * @param rating           Calificación de 1 a 5
 * @param comment          Comentario del review (máximo 1000 caracteres)
 * @param verifiedPurchase Si la compra fue verificada
 */
public record ReviewRequest(
                @NotNull(message = "El ID de usuario es obligatorio") @Positive(message = "El ID de usuario debe ser un número positivo") Long userId,

                @NotNull(message = "El ID del tour plan es obligatorio") @Positive(message = "El ID del tour plan debe ser un número positivo") Integer tourPlanId,

                @NotBlank(message = "El título es obligatorio") @Size(max = 100, message = "El título no puede exceder los 100 caracteres") String title,

                @NotNull(message = "La calificación es obligatoria") @DecimalMin(value = "1", message = "La calificación mínima es 1") @DecimalMax(value = "5", message = "La calificación máxima es 5") Integer rating,

                @Size(max = 1000, message = "El comentario no puede exceder los 1000 caracteres") String comment,

                Boolean verifiedPurchase) {
}
