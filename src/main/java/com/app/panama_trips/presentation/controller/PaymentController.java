package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.persistence.entity.Payment;
import com.app.panama_trips.presentation.dto.PaymentRequest;
import com.app.panama_trips.presentation.dto.PaymentResponse;
import com.app.panama_trips.service.interfaces.IPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Validated
@Tag(name = "Payment", description = "Endpoints for managing payments")
public class PaymentController {

    private final IPaymentService paymentService;

    @GetMapping
    @Operation(
            summary = "Get all payments",
            description = "Get all payments in the system",
            tags = {"Payment"},
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Payments found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Payment.class)
                    )
            )
    )
    public ResponseEntity<List<PaymentResponse>> findAllPayments() {
        return ResponseEntity.ok(this.paymentService.getAllPayments());
    }

    @PostMapping
    @Operation(
            summary = "Create a new payment",
            description = "Create a new payment in the system",
            tags = {"Payment"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payment data to create",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Payment.class)
                    )
            ),
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Payment created",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Payment.class)
                    )
            )
    )
    public ResponseEntity<PaymentResponse> savePayment(@RequestBody @Valid PaymentRequest paymentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.paymentService.savePayment(paymentRequest));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a payment by id",
            description = "Get a payment in the system by its id",
            tags = {"Payment"},
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Payment found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Payment.class)
                    )
            )
    )
    public ResponseEntity<PaymentResponse> findPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(this.paymentService.getPaymentById(id));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a payment",
            description = "Update a payment in the system",
            tags = {"Payment"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payment data to update",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Payment.class)
                    )
            ),
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Payment updated",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Payment.class)
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
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "Payment deleted"
            )
    )
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        this.paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
