package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.DiscountRequest;
import com.app.panama_trips.presentation.dto.DiscountResponse;
import com.app.panama_trips.service.implementation.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
@Tag(name = "Discount", description = "Endpoints for managing discounts applied to reservations")
public class DiscountController {

  private final DiscountService discountService;

  @GetMapping
  @Operation(summary = "Get all discounts", description = "Retrieve a paginated list of all discounts")
  public ResponseEntity<Page<DiscountResponse>> getAllDiscounts(Pageable pageable) {
    return ResponseEntity.ok(this.discountService.getAllDiscounts(pageable));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get discount by ID", description = "Retrieve details of a discount by its unique identifier")
  public ResponseEntity<DiscountResponse> getDiscountById(@PathVariable Long id) {
    return ResponseEntity.ok(this.discountService.getDiscountById(id));
  }

  @GetMapping("/reservation/{reservationId}")
  @Operation(summary = "Get discounts by reservation", description = "Retrieve all discounts applied to a specific reservation")
  public ResponseEntity<List<DiscountResponse>> getDiscountsByReservationId(@PathVariable Integer reservationId) {
    return ResponseEntity.ok(this.discountService.getDiscountsByReservationId(reservationId));
  }

  @PostMapping
  @Operation(summary = "Create discount", description = "Asociate a discount to a reservation")
  public ResponseEntity<DiscountResponse> saveDiscount(@Valid @RequestBody DiscountRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(this.discountService.saveDiscount(request));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update discount", description = "Update an existing discount's details")
  public ResponseEntity<DiscountResponse> updateDiscount(@PathVariable Long id,
      @Valid @RequestBody DiscountRequest request) {
    return ResponseEntity.ok(this.discountService.updateDiscount(id, request));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete discount", description = "Remove a discount from the system")
  public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
    this.discountService.deleteDiscount(id);
    return ResponseEntity.noContent().build();
  }
}
