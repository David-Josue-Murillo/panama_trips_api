package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.CouponRequest;
import com.app.panama_trips.presentation.dto.CouponResponse;
import com.app.panama_trips.service.implementation.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
@Tag(name = "Coupon", description = "Endpoints for managing discount coupons")
public class CouponController {

  private final CouponService couponService;

  @GetMapping
  @Operation(summary = "Get all coupons", description = "Retrieve a paginated list of all coupons")
  public ResponseEntity<Page<CouponResponse>> getAllCoupons(Pageable pageable) {
    return ResponseEntity.ok(this.couponService.getAllCoupons(pageable));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get coupon by ID", description = "Retrieve details of a coupon by its unique identifier")
  public ResponseEntity<CouponResponse> getCouponById(@PathVariable Long id) {
    return ResponseEntity.ok(this.couponService.getCouponById(id));
  }

  @GetMapping("/code/{code}")
  @Operation(summary = "Get coupon by code", description = "Retrieve details of a coupon by its code")
  public ResponseEntity<CouponResponse> getCouponByCode(@PathVariable String code) {
    return ResponseEntity.ok(this.couponService.getCouponByCode(code));
  }

  @GetMapping("/validate/{code}")
  @Operation(summary = "Validate coupon", description = "Check if a coupon code is valid and not expired")
  public ResponseEntity<CouponResponse> validateCoupon(@PathVariable String code) {
    return ResponseEntity.ok(this.couponService.validateAndGetCoupon(code));
  }

  
}
