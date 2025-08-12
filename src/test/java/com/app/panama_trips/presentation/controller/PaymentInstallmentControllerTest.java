package com.app.panama_trips.presentation.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.app.panama_trips.presentation.dto.PaymentInstallmentRequest;
import com.app.panama_trips.presentation.dto.PaymentInstallmentResponse;
import com.app.panama_trips.service.implementation.PaymentInstallmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static com.app.panama_trips.DataProvider.*;


@WebMvcTest(PaymentInstallmentController.class)
public class PaymentInstallmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentInstallmentService service;

    private PaymentInstallmentRequest request;
    private PaymentInstallmentResponse response;
    private List<PaymentInstallmentResponse> responseList;

    @BeforeEach
    void setUp() {
      request = paymentInstallmentRequestMock;
      response = paymentInstallmentResponseMock;
      responseList = paymentInstallmentResponseListMock; 
    }

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      return mapper.writeValueAsString(obj);
  }
}
