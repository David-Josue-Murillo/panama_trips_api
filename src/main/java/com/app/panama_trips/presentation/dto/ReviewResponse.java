package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.Review;

import java.time.LocalDateTime;

/**
 * DTO para responder con datos de un review.
 *
 * @param id                 ID del review
 * @param userId             ID del usuario que escribió el review
 * @param userName           Nombre completo del usuario
 * @param userImageUrl       URL de la imagen de perfil del usuario
 * @param tourPlanId         ID del tour plan evaluado
 * @param tourPlanTitle      Título del tour plan evaluado
 * @param title              Título del review
 * @param rating             Calificación (1-5)
 * @param comment            Comentario del review
 * @param verifiedPurchase   Si la compra fue verificada
 * @param responseByProvider Respuesta del proveedor al review
 * @param responseDate       Fecha de la respuesta del proveedor
 * @param helpfulVotes       Cantidad de votos útiles
 * @param reported           Si el review fue reportado
 * @param status             Estado del review (ACTIVE, PENDING, REMOVED)
 * @param createdAt          Fecha de creación del review
 */
public record ReviewResponse(
        Long id,
        Long userId,
        String userName,
        String userImageUrl,
        Integer tourPlanId,
        String tourPlanTitle,
        String title,
        Integer rating,
        String comment,
        Boolean verifiedPurchase,
        String responseByProvider,
        LocalDateTime responseDate,
        Integer helpfulVotes,
        Boolean reported,
        String status,
        LocalDateTime createdAt
) {
    public ReviewResponse(Review review) {
        this(
                review.getId(),
                review.getUserId().getId(),
                review.getUserId().getName() + " " + review.getUserId().getLastname(),
                review.getUserId().getProfileImageUrl(),
                review.getTourPlanId().getId(),
                review.getTourPlanId().getTitle(),
                review.getTitle(),
                review.getRating(),
                review.getComment(),
                review.getVerifiedPurchase(),
                review.getResponseByProvider(),
                review.getResponseDate(),
                review.getHelpfulVotes(),
                review.getReported(),
                review.getStatus(),
                review.getCreatedAt()
        );
    }
}
