package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.persistence.entity.Payment;
import com.app.panama_trips.persistence.entity.PaymentStatus;
import com.app.panama_trips.presentation.dto.PaymentRequest;
import com.app.panama_trips.presentation.dto.PaymentResponse;
import com.app.panama_trips.service.interfaces.IPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Validated
@Tag(name = "Payment", description = "Endpoints for managing payments")
public class PaymentController {

    private final IPaymentService paymentService;

    // ─────────────────────────────────────────────
    //  CRUD básico
    // ─────────────────────────────────────────────

    @GetMapping
    @Operation(
            summary = "Get all payments",
            description = "Get all payments in the system",
            tags = {"Payment"},
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Payments found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class)
                    )
            )
    )
    public ResponseEntity<List<PaymentResponse>> findAllPayments() {
        return ResponseEntity.ok(this.paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a payment by id",
            description = "Get a payment in the system by its id",
            tags = {"Payment"},
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Payment found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class)
                    )
            )
    )
    public ResponseEntity<PaymentResponse> findPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(this.paymentService.getPaymentById(id));
    }

    @PostMapping
    @Operation(
            summary = "Create a new payment",
            description = "Create a new payment in the system",
            tags = {"Payment"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payment data to create",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class)
                    )
            ),
            responses = @ApiResponse(
                    responseCode = "201",
                    description = "Payment created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class)
                    )
            )
    )
    public ResponseEntity<PaymentResponse> savePayment(@RequestBody @Valid PaymentRequest paymentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.paymentService.savePayment(paymentRequest));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a payment",
            description = "Update a payment in the system",
            tags = {"Payment"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payment data to update",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class)
                    )
            ),
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Payment updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class)
                    )
            )
    )
    public ResponseEntity<PaymentResponse> updatePayment(@PathVariable Long id, @RequestBody @Valid PaymentRequest paymentRequest) {
        return ResponseEntity.ok(this.paymentService.updatePayment(id, paymentRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a payment",
            description = "Delete a payment in the system",
            tags = {"Payment"},
            responses = @ApiResponse(
                    responseCode = "204",
                    description = "Payment deleted"
            )
    )
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        this.paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    // ─────────────────────────────────────────────
    //  Búsquedas / Filtros
    // ─────────────────────────────────────────────

    @GetMapping("/status/{status}")
    @Operation(
            summary = "Get payments by status",
            description = "Returns all payments that match the given status (PENDING, COMPLETED, FAILED)",
            tags = {"Payment"},
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Payments found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponse.class))
            )
    )
    public ResponseEntity<List<PaymentResponse>> findPaymentsByStatus(
            @PathVariable @Parameter(description = "Payment status: PENDING, COMPLETED or FAILED") PaymentStatus status) {
        return ResponseEntity.ok(this.paymentService.getPaymentsByStatus(status));
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Get payments by user",
            description = "Returns all payments associated with a specific user",
            tags = {"Payment"},
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Payments found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponse.class))
            )
    )
    public ResponseEntity<List<PaymentResponse>> findPaymentsByUser(
            @PathVariable @Parameter(description = "User ID") Long userId) {
        return ResponseEntity.ok(this.paymentService.getPaymentsByUser(userId));
    }

    @GetMapping("/reservation/{reservationId}")
    @Operation(
            summary = "Get payments by reservation",
            description = "Returns all payments associated with a specific reservation",
            tags = {"Payment"},
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Payments found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponse.class))
            )
    )
    public ResponseEntity<List<PaymentResponse>> findPaymentsByReservation(
            @PathVariable @Parameter(description = "Reservation ID") Long reservationId) {
        return ResponseEntity.ok(this.paymentService.getPaymentsByReservation(reservationId));
    }

    @GetMapping("/date-range")
    @Operation(
            summary = "Get payments by date range",
            description = "Returns all payments whose creation date falls within the given range. " +
                    "Date format: yyyy-MM-dd'T'HH:mm:ss (e.g. 2025-01-01T00:00:00)",
            tags = {"Payment"},
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Payments found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponse.class))
            )
    )
    public ResponseEntity<List<PaymentResponse>> findPaymentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Parameter(description = "Start date (ISO format: yyyy-MM-dd'T'HH:mm:ss)") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Parameter(description = "End date (ISO format: yyyy-MM-dd'T'HH:mm:ss)") LocalDateTime endDate) {
        return ResponseEntity.ok(this.paymentService.getPaymentsByDateRange(startDate, endDate));
    }

    // ─────────────────────────────────────────────
    //  Estadísticas
    // ─────────────────────────────────────────────

    @GetMapping("/count/status/{status}")
    @Operation(
            summary = "Count payments by status",
            description = "Returns the total number of payments that have the given status",
            tags = {"Payment"},
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Count returned",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class))
            )
    )
    public ResponseEntity<Long> countPaymentsByStatus(
            @PathVariable @Parameter(description = "Payment status: PENDING, COMPLETED or FAILED") PaymentStatus status) {
        return ResponseEntity.ok(this.paymentService.countPaymentsByStatus(status));
    }

    // ─────────────────────────────────────────────
    //  Acciones de estado
    // ─────────────────────────────────────────────

    @PatchMapping("/{id}/approve")
    @Operation(
            summary = "Approve a payment",
            description = "Sets the payment status to COMPLETED",
            tags = {"Payment"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payment approved",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PaymentResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Payment not found")
            }
    )
    public ResponseEntity<PaymentResponse> approvePayment(@PathVariable Long id) {
        return ResponseEntity.ok(this.paymentService.approvePayment(id));
    }

    @PatchMapping("/{id}/reject")
    @Operation(
            summary = "Reject a payment",
            description = "Sets the payment status to FAILED",
            tags = {"Payment"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payment rejected",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PaymentResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Payment not found")
            }
    )
    public ResponseEntity<PaymentResponse> rejectPayment(@PathVariable Long id) {
        return ResponseEntity.ok(this.paymentService.rejectPayment(id));
    }
}

