package com.app.panama_trips.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.PaymentInstallment;
import com.app.panama_trips.persistence.repository.PaymentInstallmentRepository;
import com.app.panama_trips.persistence.repository.PaymentRepository;
import com.app.panama_trips.persistence.repository.ReservationRepository;
import com.app.panama_trips.presentation.dto.PaymentInstallmentRequest;
import com.app.panama_trips.presentation.dto.PaymentInstallmentResponse;
import com.app.panama_trips.service.implementation.PaymentInstallmentService;

import static com.app.panama_trips.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentInstallmentServiceTest {

  @Mock
  private PaymentInstallmentRepository repository;

  @Mock
  private ReservationRepository reservationRepository;

  @Mock
  private PaymentRepository paymentRepository;

  @InjectMocks
  private PaymentInstallmentService service;

  @Captor
  private ArgumentCaptor<PaymentInstallment> installmentCaptor;

  @Captor
  private ArgumentCaptor<List<PaymentInstallment>> installmentsCaptor;

  private PaymentInstallment paymentInstallment;
  private PaymentInstallmentRequest request;
  private List<PaymentInstallment> paymentInstallments;

  @BeforeEach
  void setUp() {
    paymentInstallment = paymentInstallmentOneMock();
    request = paymentInstallmentRequestMock;
    paymentInstallments = paymentInstallmentListMock();
  }

  
}
