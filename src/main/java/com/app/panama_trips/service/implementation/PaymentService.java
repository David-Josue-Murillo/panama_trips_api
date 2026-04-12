package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Payment;
import com.app.panama_trips.persistence.entity.PaymentStatus;
import com.app.panama_trips.persistence.entity.Reservation;
import com.app.panama_trips.persistence.repository.PaymentRepository;
import com.app.panama_trips.persistence.repository.ReservationRepository;
import com.app.panama_trips.presentation.dto.PaymentRequest;
import com.app.panama_trips.presentation.dto.PaymentResponse;
import com.app.panama_trips.service.interfaces.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        return this.paymentRepository.findAll().stream().map(PaymentResponse::new).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long id) {
        return this.paymentRepository.findById(id)
                .map(PaymentResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id " + id));
    }

    @Override
    @Transactional
    public PaymentResponse savePayment(PaymentRequest request) {
        Reservation reservation = this.reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + request.reservationId()));

        Payment payment = new Payment();
        // Set relations and core fields
        payment.setReservationId(reservation);
        payment.setTransactionId(request.transactionId());
        payment.setAmount(request.amount());
        
        if (request.paymentStatus() != null) {
            payment.setPaymentStatus(request.paymentStatus());
        } else {
            payment.setPaymentStatus(PaymentStatus.PENDING);
        }

        payment.setCreatedAt(LocalDateTime.now());
        
        // Optional refund and specific fields
        payment.setRefundAmount(request.refundAmount());
        payment.setRefundReason(request.refundReason());
        payment.setRefundDate(request.refundDate());
        payment.setPaymentMethod(request.paymentMethod());
        payment.setPaymentDetails(request.paymentDetails());

        return new PaymentResponse(this.paymentRepository.save(payment));
    }

    @Override
    @Transactional
    public PaymentResponse updatePayment(Long id, PaymentRequest request) {
        Payment existingPayment = this.paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id " + id));

        // It's uncommon to allow changing the reservationId of a payment, but we will support it for full CRUD completeness
        if (!existingPayment.getReservationId().getId().equals(request.reservationId())) {
            Reservation updatedReservation = this.reservationRepository.findById(request.reservationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + request.reservationId()));
            existingPayment.setReservationId(updatedReservation);
        }

        existingPayment.setTransactionId(request.transactionId());
        existingPayment.setAmount(request.amount());
        
        if (request.paymentStatus() != null) {
            existingPayment.setPaymentStatus(request.paymentStatus());
        }

        existingPayment.setRefundAmount(request.refundAmount());
        existingPayment.setRefundReason(request.refundReason());
        existingPayment.setRefundDate(request.refundDate());
        existingPayment.setPaymentMethod(request.paymentMethod());
        existingPayment.setPaymentDetails(request.paymentDetails());

        return new PaymentResponse(this.paymentRepository.save(existingPayment));
    }

    @Override
    @Transactional
    public void deletePayment(Long id) {
        if (!this.paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment not found with id " + id);
        }
        this.paymentRepository.deleteById(id);
    }
}
