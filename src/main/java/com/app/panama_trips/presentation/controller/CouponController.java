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

  
}
