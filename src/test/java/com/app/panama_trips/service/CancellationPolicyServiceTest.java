package com.app.panama_trips.service;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.persistence.entity.CancellationPolicy;
import com.app.panama_trips.persistence.repository.CancellationPolicyRepository;
import com.app.panama_trips.presentation.dto.CancellationPolicyRequest;
import com.app.panama_trips.presentation.dto.CancellationPolicyResponse;
import com.app.panama_trips.service.implementation.CancellationPolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.app.panama_trips.DataProvider.*;

@ExtendWith(MockitoExtension.class)
public class CancellationPolicyServiceTest {

    @Mock
    private CancellationPolicyRepository repository;

    @InjectMocks
    private CancellationPolicyService service;

    private CancellationPolicy cancellationPolicy;
    private CancellationPolicyRequest request;
    private CancellationPolicyResponse response;
    private List<CancellationPolicy> cancellationPolicies;

    @BeforeEach
    void setUp() {
        cancellationPolicy = cancellationPolicyOneMock;
        cancellationPolicies = cancellationPolicyListMock;
        request = cancellationPolicyRequestMock;
        response = cancellationPolicyResponseMock;
    }

}
