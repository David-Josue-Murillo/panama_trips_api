package com.app.panama_trips.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.panama_trips.service.implementation.PaymentInstallmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payment-installment")
@RequiredArgsConstructor
public class PaymentInstallmentRepository {
    private final PaymentInstallmentService service;
}
