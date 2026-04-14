package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.ReviewRequest;
import com.app.panama_trips.presentation.dto.ReviewResponse;
import com.app.panama_trips.service.interfaces.IReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Validated
@Tag(name = "Review", description = "Endpoints para gestionar comentarios y calificaciones de tours")
public class ReviewController {

    private final IReviewService reviewService;

    // ==================== CRUD Operations ====================

    @GetMapping
    @Operation(
            summary = "Obtener todos los reviews",
            description = "Obtiene una lista paginada de todos los reviews en el sistema",
            tags = {"Review"},
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Reviews encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponse.class)
                    )
            )
    )
    public ResponseEntity<Page<ReviewResponse>> getAllReviews(
            @Parameter(description = "Número de página (default: 0)")
            @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Tamaño de página (default: 10)")
            @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "Habilitar paginación (default: false)")
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(reviewService.getAllReviews(pageable));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener review por ID",
            description = "Obtiene los detalles de un review específico por su ID",
            tags = {"Review"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Review encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReviewResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Review no encontrado"
                    )
            }
    )
    public ResponseEntity<ReviewResponse> getReviewById(
            @Parameter(description = "ID del review", required = true)
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @PostMapping
    @Operation(
            summary = "Crear nuevo review",
            description = "Crea un nuevo review para un tour plan. El usuario no puede tener más de un review por tour.",
            tags = {"Review"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del review a crear",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Review creado exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReviewResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos inválidos o usuario ya escribió un review para este tour"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuario o Tour Plan no encontrados"
                    )
            }
    )
    public ResponseEntity<ReviewResponse> createReview(
            @Valid @RequestBody ReviewRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.saveReview(request));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar review existente",
            description = "Actualiza los datos de un review existente",
            tags = {"Review"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevos datos del review",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Review actualizado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReviewResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Review no encontrado"
                    )
            }
    )
    public ResponseEntity<ReviewResponse> updateReview(
            @Parameter(description = "ID del review a actualizar", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ReviewRequest request
    ) {
        return ResponseEntity.ok(reviewService.updateReview(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar review",
            description = "Elimina un review del sistema",
            tags = {"Review"},
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Review eliminado"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Review no encontrado"
                    )
            }
    )
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "ID del review a eliminar", required = true)
            @PathVariable Long id
    ) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== Query Methods ====================

    @GetMapping("/tour-plan/{tourPlanId}")
    @Operation(
            summary = "Obtener reviews por tour plan",
            description = "Obtiene todos los reviews de un tour plan específico",
            tags = {"Review"},
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Reviews del tour plan encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponse.class)
                    )
            )
    )
    public ResponseEntity<Page<ReviewResponse>> getReviewsByTourPlanId(
            @Parameter(description = "ID del tour plan", required = true)
            @PathVariable Integer tourPlanId,
            @Parameter(description = "Número de página (default: 0)")
            @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Tamaño de página (default: 10)")
            @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "Habilitar paginación (default: false)")
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(reviewService.getReviewsByTourPlanId(tourPlanId, pageable));
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Obtener reviews por usuario",
            description = "Obtiene todos los reviews escritos por un usuario específico",
            tags = {"Review"},
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Reviews del usuario encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponse.class)
                    )
            )
    )
    public ResponseEntity<Page<ReviewResponse>> getReviewsByUserId(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long userId,
            @Parameter(description = "Número de página (default: 0)")
            @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Tamaño de página (default: 10)")
            @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "Habilitar paginación (default: false)")
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(reviewService.getReviewsByUserId(userId, pageable));
    }

    @GetMapping("/status/{status}")
    @Operation(
            summary = "Obtener reviews por estado",
            description = "Obtiene los reviews filtrados por su estado (ACTIVE, PENDING, REMOVED)",
            tags = {"Review"},
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Reviews encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponse.class)
                    )
            )
    )
    public ResponseEntity<Page<ReviewResponse>> getReviewsByStatus(
            @Parameter(description = "Estado del review (ACTIVE, PENDING, REMOVED)", required = true)
            @PathVariable String status,
            @Parameter(description = "Número de página (default: 0)")
            @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Tamaño de página (default: 10)")
            @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "Habilitar paginación (default: false)")
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(reviewService.getReviewsByStatus(status, pageable));
    }

    @GetMapping("/tour-plan/{tourPlanId}/top")
    @Operation(
            summary = "Obtener reviews más útiles",
            description = "Obtiene los reviews más útiles (mayor cantidad de helpful_votes) de un tour plan",
            tags = {"Review"},
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Reviews más útiles encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponse.class)
                    )
            )
    )
    public ResponseEntity<Page<ReviewResponse>> getTopReviewsByTourPlanId(
            @Parameter(description = "ID del tour plan", required = true)
            @PathVariable Integer tourPlanId,
            @Parameter(description = "Número de página (default: 0)")
            @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Tamaño de página (default: 10)")
            @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "Habilitar paginación (default: false)")
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(reviewService.getTopReviewsByTourPlanId(tourPlanId, pageable));
    }

    @GetMapping("/tour-plan/{tourPlanId}/verified")
    @Operation(
            summary = "Obtener reviews verificados",
            description = "Obtiene solo los reviews con compra verificada de un tour plan",
            tags = {"Review"},
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Reviews verificados encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponse.class)
                    )
            )
    )
    public ResponseEntity<Page<ReviewResponse>> getVerifiedReviewsByTourPlanId(
            @Parameter(description = "ID del tour plan", required = true)
            @PathVariable Integer tourPlanId,
            @Parameter(description = "Número de página (default: 0)")
            @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Tamaño de página (default: 10)")
            @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "Habilitar paginación (default: false)")
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(reviewService.getVerifiedReviewsByTourPlanId(tourPlanId, pageable));
    }

    // ==================== Statistics ====================

    @GetMapping("/tour-plan/{tourPlanId}/average")
    @Operation(
            summary = "Obtener calificación promedio",
            description = "Calcula la calificación promedio de un tour plan (solo reviews ACTIVE)",
            tags = {"Review"},
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Promedio de calificación (0.0 - 5.0)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Double.class)
                    )
            )
    )
    public ResponseEntity<Double> getAverageRatingByTourPlanId(
            @Parameter(description = "ID del tour plan", required = true)
            @PathVariable Integer tourPlanId
    ) {
        return ResponseEntity.ok(reviewService.getAverageRatingByTourPlanId(tourPlanId));
    }

    @GetMapping("/tour-plan/{tourPlanId}/statistics")
    @Operation(
            summary = "Obtener estadísticas de reviews",
            description = "Obtiene estadísticas completas de reviews de un tour plan (total, promedio, verificados)",
            tags = {"Review"},
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Estadísticas de reviews",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)
                    )
            )
    )
    public ResponseEntity<Map<String, Object>> getReviewStatisticsByTourPlanId(
            @Parameter(description = "ID del tour plan", required = true)
            @PathVariable Integer tourPlanId
    ) {
        return ResponseEntity.ok(reviewService.getReviewStatisticsByTourPlanId(tourPlanId));
    }

    @GetMapping("/tour-plan/{tourPlanId}/status-count")
    @Operation(
            summary = "Contar reviews por estado",
            description = "Obtiene la cantidad de reviews por estado para un tour plan",
            tags = {"Review"},
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Conteo de reviews por estado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)
                    )
            )
    )
    public ResponseEntity<Map<String, Long>> countReviewsByStatusForTourPlan(
            @Parameter(description = "ID del tour plan", required = true)
            @PathVariable Integer tourPlanId
    ) {
        return ResponseEntity.ok(reviewService.countReviewsByStatusForTourPlan(tourPlanId));
    }

    // ==================== Actions ====================

    @PatchMapping("/{id}/response")
    @Operation(
            summary = "Responder a un review",
            description = "Permite a un proveedor responder a un review. Solo puede ser respondido una vez.",
            tags = {"Review"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Respuesta del proveedor",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"response\": \"Gracias por tu comentario, esperamos verte pronto.\", \"updatedByUserId\": 1}")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Respuesta registrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReviewResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Review no encontrado"
                    )
            }
    )
    public ResponseEntity<ReviewResponse> respondToReview(
            @Parameter(description = "ID del review", required = true)
            @PathVariable Long id,
            @RequestBody Map<String, Object> body
    ) {
        String response = (String) body.get("response");
        Long updatedByUserId = ((Number) body.get("updatedByUserId")).longValue();
        return ResponseEntity.ok(reviewService.respondToReview(id, response, updatedByUserId));
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Actualizar estado del review",
            description = "Actualiza el estado de un review (ACTIVE, PENDING, REMOVED). Usado por administradores.",
            tags = {"Review"},
            parameters = @Parameter(
                    name = "status",
                    description = "Nuevo estado (ACTIVE, PENDING, REMOVED)",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Estado actualizado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReviewResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Estado inválido"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Review no encontrado"
                    )
            }
    )
    public ResponseEntity<ReviewResponse> updateReviewStatus(
            @Parameter(description = "ID del review", required = true)
            @PathVariable Long id,
            @RequestParam String status
    ) {
        return ResponseEntity.ok(reviewService.updateReviewStatus(id, status));
    }

    @PostMapping("/{id}/report")
    @Operation(
            summary = "Reportar un review",
            description = "Marca un review como reportado para revisión moderación",
            tags = {"Review"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Review reportado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReviewResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Review no encontrado"
                    )
            }
    )
    public ResponseEntity<ReviewResponse> reportReview(
            @Parameter(description = "ID del review", required = true)
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(reviewService.reportReview(id));
    }

    @PostMapping("/{id}/helpful")
    @Operation(
            summary = "Marcar review como útil",
            description = "Incrementa el contador de votos útiles de un review",
            tags = {"Review"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Voto registrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReviewResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Review no encontrado"
                    )
            }
    )
    public ResponseEntity<ReviewResponse> addHelpfulVote(
            @Parameter(description = "ID del review", required = true)
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(reviewService.addHelpfulVote(id));
    }

    @GetMapping("/check/{userId}/tour-plan/{tourPlanId}")
    @Operation(
            summary = "Verificar si usuario ya escribió review",
            description = "Verifica si un usuario ya escribió un review para un tour plan específico",
            tags = {"Review"},
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "true si ya escribió un review, false en caso contrario",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class)
                    )
            )
    )
    public ResponseEntity<Boolean> userAlreadyReviewed(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID del tour plan", required = true)
            @PathVariable Integer tourPlanId
    ) {
        return ResponseEntity.ok(reviewService.userAlreadyReviewed(userId, tourPlanId));
    }
}
