package com.app.panama_trips.service;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.panama_trips.persistence.entity.PaymentInstallment;
import com.app.panama_trips.persistence.repository.PaymentInstallmentRepository;
import com.app.panama_trips.presentation.dto.PaymentInstallmentRequest;
import com.app.panama_trips.service.implementation.PaymentInstallmentService;

import static com.app.panama_trips.DataProvider.*;

@ExtendWith(MockitoExtension.class)
public class PaymentInstallmentServiceTest {
  
  @Mock
  private PaymentInstallmentRepository repository;

  @InjectMocks
  private PaymentInstallmentService service;

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
