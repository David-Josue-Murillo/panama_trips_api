package com.app.panama_trips.service.implementation;

import org.springframework.stereotype.Service;

import com.app.panama_trips.persistence.repository.PaymentInstallmentRepository;
import com.app.panama_trips.persistence.repository.PaymentRepository;
import com.app.panama_trips.persistence.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentInstallmentService {

    private final PaymentInstallmentRepository repository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
}
