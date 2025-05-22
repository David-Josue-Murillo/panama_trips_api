package com.app.panama_trips.service;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.CancellationPolicy;
import com.app.panama_trips.persistence.repository.CancellationPolicyRepository;
import com.app.panama_trips.presentation.dto.CancellationPolicyRequest;
import com.app.panama_trips.presentation.dto.CancellationPolicyResponse;
import com.app.panama_trips.service.implementation.CancellationPolicyService;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.app.panama_trips.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CancellationPolicyServiceTest {

    @Mock
    private CancellationPolicyRepository repository;

    @InjectMocks
    private CancellationPolicyService service;

    @Captor
    private ArgumentCaptor<CancellationPolicy> cancellationPolicyCaptor;

    @Captor
    private ArgumentCaptor<List<CancellationPolicy>> policiesCaptor;

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

    @Test
    @DisplayName("Should return all cancellation policies when getAllCancellationPolicies is called with pagination")
    void getAllCancellationPolicies_shouldReturnAllData() {
        // Given
        Page<CancellationPolicy> page = new PageImpl<>(cancellationPolicies);
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(page);

        // When
        Page<CancellationPolicyResponse> response = service.getAllCancellationPolicies(pageable);

        // Then
        assertNotNull(response);
        assertEquals(cancellationPolicies.size(), response.getTotalElements());
        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("Should return cancellation policy by id when exists")
    void getCancellationPolicyById_whenExists_shouldReturnPolicy() {
        // Given
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.of(cancellationPolicy));

        // When
        CancellationPolicyResponse result = service.getCancellationPolicyById(id);

        // Then
        assertNotNull(result);
        assertEquals(cancellationPolicy.getId(), result.id());
        assertEquals(cancellationPolicy.getName(), result.name());
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Should throw exception when getting cancellation policy by id that doesn't exist")
    void getCancellationPolicyById_whenNotExists_shouldThrowException() {
        // Given
        Integer id = 999;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getCancellationPolicyById(id)
        );
        assertEquals("Cancellation Policy not found with id: " + id, exception.getMessage());
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Should save cancellation policy successfully")
    void saveCancellationPolicy_success() {
        // Given
        when(repository.findByName(request.name())).thenReturn(Optional.empty());
        when(repository.save(any(CancellationPolicy.class))).thenReturn(cancellationPolicy);

        // When
        CancellationPolicyResponse result = service.saveCancellationPolicy(request);

        // Then
        assertNotNull(result);
        assertEquals(cancellationPolicy.getId(), result.id());
        assertEquals(cancellationPolicy.getName(), result.name());

        verify(repository).findByName(request.name());
        verify(repository).save(cancellationPolicyCaptor.capture());

        CancellationPolicy savedPolicy = cancellationPolicyCaptor.getValue();
        assertEquals(request.name(), savedPolicy.getName());
        assertEquals(request.description(), savedPolicy.getDescription());
        assertEquals(request.refundPercentage(), savedPolicy.getRefundPercentage());
        assertEquals(request.daysBeforeTour(), savedPolicy.getDaysBeforeTour());
    }

    @Test
    @DisplayName("Should throw exception when saving policy with duplicate name")
    void saveCancellationPolicy_withDuplicateName_shouldThrowException() {
        // Given
        when(repository.findByName(request.name())).thenReturn(Optional.of(cancellationPolicy));

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.saveCancellationPolicy(request)
        );
        assertEquals("A cancellation policy with this name already exists", exception.getMessage());
        verify(repository).findByName(request.name());
        verify(repository, never()).save(any(CancellationPolicy.class));
    }

    @Test
    @DisplayName("Should throw exception when validating policy with high refund and low days notice")
    void saveCancellationPolicy_withHighRefundAndLowDays_shouldThrowException() {
        // Given
        CancellationPolicyRequest invalidRequest = new CancellationPolicyRequest(
                "High Refund Policy",
                "Description",
                80, // high percentage
                5   // low days notice
        );

        when(repository.findByName(invalidRequest.name())).thenReturn(Optional.empty());

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.saveCancellationPolicy(invalidRequest)
        );
        assertEquals("High refund percentages (>75%) should typically require at least 7 days notice",
                exception.getMessage());
        verify(repository).findByName(invalidRequest.name());
        verify(repository, never()).save(any(CancellationPolicy.class));
    }

    @Test
    @DisplayName("Should update cancellation policy successfully")
    void updateCancellationPolicy_success() {
        // Given
        Integer id = 1;
        CancellationPolicyRequest updateRequest = new CancellationPolicyRequest(
                "Updated Policy",
                "Updated Description",
                50,
                10
        );

        when(repository.findById(id)).thenReturn(Optional.of(cancellationPolicy));
        when(repository.findByName(updateRequest.name())).thenReturn(Optional.empty());
        when(repository.save(any(CancellationPolicy.class))).thenReturn(cancellationPolicy);

        // When
        CancellationPolicyResponse result = service.updateCancellationPolicy(id, updateRequest);

        // Then
        assertNotNull(result);

        verify(repository).findById(id);
        verify(repository).findByName(updateRequest.name());
        verify(repository).save(cancellationPolicyCaptor.capture());

        CancellationPolicy updatedPolicy = cancellationPolicyCaptor.getValue();
        assertEquals(updateRequest.name(), updatedPolicy.getName());
        assertEquals(updateRequest.description(), updatedPolicy.getDescription());
        assertEquals(updateRequest.refundPercentage(), updatedPolicy.getRefundPercentage());
        assertEquals(updateRequest.daysBeforeTour(), updatedPolicy.getDaysBeforeTour());
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent policy")
    void deleteCancellationPolicy_whenNotExists_shouldThrowException() {
        // Given
        Integer id = 999;
        when(repository.existsById(id)).thenReturn(false);

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.deleteCancellationPolicy(id)
        );
        assertEquals("Cancellation Policy not found with id: " + id, exception.getMessage());
        verify(repository).existsById(id);
        verify(repository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Should delete policy successfully when not in use")
    void deleteCancellationPolicy_whenNotInUse_success() {
        // Given
        Integer id = 1;
        when(repository.existsById(id)).thenReturn(true);

        // When
        service.deleteCancellationPolicy(id);

        // Then
        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw exception when deleting policy in use")
    void deleteCancellationPolicy_whenInUse_shouldThrowException() {
        // Given
        Integer id = 1;

        // Mock the service to return true for isPolicyUsedByAnyTour
        CancellationPolicyService spyService = spy(service);
        doReturn(true).when(spyService).isPolicyUsedByAnyTour(id);
        when(repository.existsById(id)).thenReturn(true);

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> spyService.deleteCancellationPolicy(id)
        );
        assertEquals("Cannot delete a cancellation policy that is being used by tours", exception.getMessage());
        verify(repository).existsById(id);
        verify(repository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Should find policies by refund percentage")
    void findByRefundPercentageGreaterThanEqual_shouldReturnMatchingPolicies() {
        // Given
        Integer minPercentage = 20;
        when(repository.findByRefundPercentageGreaterThanEqual(minPercentage))
                .thenReturn(cancellationPolicies);

        // When
        List<CancellationPolicyResponse> result = service.findByRefundPercentageGreaterThanEqual(minPercentage);

        // Then
        assertNotNull(result);
        assertEquals(cancellationPolicies.size(), result.size());
        verify(repository).findByRefundPercentageGreaterThanEqual(minPercentage);
    }

    @Test
    @DisplayName("Should find policies by days before tour")
    void findByDaysBeforeTourGreaterThanEqual_shouldReturnMatchingPolicies() {
        // Given
        Integer minDays = 5;
        when(repository.findByDaysBeforeTourGreaterThanEqual(minDays))
                .thenReturn(cancellationPolicies);

        // When
        List<CancellationPolicyResponse> result = service.findByDaysBeforeTourGreaterThanEqual(minDays);

        // Then
        assertNotNull(result);
        assertEquals(cancellationPolicies.size(), result.size());
        verify(repository).findByDaysBeforeTourGreaterThanEqual(minDays);
    }

    @Test
    @DisplayName("Should find policy by name when exists")
    void findByName_whenExists_shouldReturnPolicy() {
        // Given
        String name = "Test one";
        when(repository.findByName(name)).thenReturn(Optional.of(cancellationPolicy));

        // When
        Optional<CancellationPolicyResponse> result = service.findByName(name);

        // Then
        assertTrue(result.isPresent());
        assertEquals(cancellationPolicy.getId(), result.get().id());
        assertEquals(name, result.get().name());
        verify(repository).findByName(name);
    }

    @Test
    @DisplayName("Should return empty optional when policy name doesn't exist")
    void findByName_whenNotExists_shouldReturnEmpty() {
        // Given
        String name = "Non-existent Policy";
        when(repository.findByName(name)).thenReturn(Optional.empty());

        // When
        Optional<CancellationPolicyResponse> result = service.findByName(name);

        // Then
        assertFalse(result.isPresent());
        verify(repository).findByName(name);
    }

    @Test
    @DisplayName("Should find eligible policies based on percentage and days")
    void findEligiblePolicies_shouldReturnMatchingPolicies() {
        // Given
        Integer minPercentage = 20;
        Integer maxDays = 10;
        when(repository.findEligiblePolicies(minPercentage, maxDays))
                .thenReturn(cancellationPolicies);

        // When
        List<CancellationPolicyResponse> result = service.findEligiblePolicies(minPercentage, maxDays);

        // Then
        assertNotNull(result);
        assertEquals(cancellationPolicies.size(), result.size());
        verify(repository).findEligiblePolicies(minPercentage, maxDays);
    }

    @Test
    @DisplayName("Should return recommended policy with highest refund percentage")
    void getRecommendedPolicy_success() {
        // Given
        Integer daysBeforeTrip = 7;

        // Create policies with different refund percentages
        CancellationPolicy policy1 = CancellationPolicy.builder()
                .id(1)
                .name("Policy 1")
                .refundPercentage(50)
                .daysBeforeTour(5)
                .build();

        CancellationPolicy policy2 = CancellationPolicy.builder()
                .id(2)
                .name("Policy 2")
                .refundPercentage(75)  // highest percentage
                .daysBeforeTour(6)
                .build();

        CancellationPolicy policy3 = CancellationPolicy.builder()
                .id(3)
                .name("Policy 3")
                .refundPercentage(25)
                .daysBeforeTour(3)
                .build();

        List<CancellationPolicy> eligiblePolicies = List.of(policy1, policy2, policy3);
        when(repository.findByDaysBeforeTourGreaterThanEqual(daysBeforeTrip))
                .thenReturn(eligiblePolicies);

        // When
        CancellationPolicyResponse result = service.getRecommendedPolicy(daysBeforeTrip);

        // Then
        assertNotNull(result);
        assertEquals(policy2.getId(), result.id());  // Should be policy2 with 75% refund
        assertEquals(policy2.getRefundPercentage(), result.refundPercentage());
        verify(repository).findByDaysBeforeTourGreaterThanEqual(daysBeforeTrip);
    }

    @Test
    @DisplayName("Should throw exception when no eligible policies found")
    void getRecommendedPolicy_whenNoEligiblePolicies_shouldThrowException() {
        // Given
        Integer daysBeforeTrip = 30;
        when(repository.findByDaysBeforeTourGreaterThanEqual(daysBeforeTrip))
                .thenReturn(Collections.emptyList());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getRecommendedPolicy(daysBeforeTrip)
        );
        assertEquals("No eligible cancellation policy found for 30 days before tour", exception.getMessage());
        verify(repository).findByDaysBeforeTourGreaterThanEqual(daysBeforeTrip);
    }

    @Test
    @DisplayName("Should determine if policy is eligible for refund based on days remaining")
    void isPolicyEligibleForRefund_whenEligible_returnsTrue() {
        // Given
        Integer policyId = 1;
        Integer daysRemaining = 10;

        CancellationPolicy policy = CancellationPolicy.builder()
                .id(policyId)
                .name("Test Policy")
                .daysBeforeTour(7) // requires 7 days notice
                .build();

        when(repository.findById(policyId)).thenReturn(Optional.of(policy));

        // When
        boolean result = service.isPolicyEligibleForRefund(policyId, daysRemaining);

        // Then
        assertTrue(result);  // 10 days remaining > 7 days required
        verify(repository).findById(policyId);
    }

    @Test
    @DisplayName("Should determine if policy is not eligible for refund based on days remaining")
    void isPolicyEligibleForRefund_whenNotEligible_returnsFalse() {
        // Given
        Integer policyId = 1;
        Integer daysRemaining = 5;

        CancellationPolicy policy = CancellationPolicy.builder()
                .id(policyId)
                .name("Test Policy")
                .daysBeforeTour(7) // requires 7 days notice
                .build();

        when(repository.findById(policyId)).thenReturn(Optional.of(policy));

        // When
        boolean result = service.isPolicyEligibleForRefund(policyId, daysRemaining);

        // Then
        assertFalse(result);  // 5 days remaining < 7 days required
        verify(repository).findById(policyId);
    }

    @Test
    @DisplayName("Should calculate correct refund amount when eligible")
    void calculateRefundAmount_whenEligible_returnsCorrectAmount() {
        // Given
        Integer policyId = 1;
        Integer totalAmount = 1000;
        Integer daysRemaining = 10;

        CancellationPolicy policy = CancellationPolicy.builder()
                .id(policyId)
                .name("Test Policy")
                .refundPercentage(75) // 75% refund
                .daysBeforeTour(7)    // requires 7 days notice
                .build();

        when(repository.findById(policyId)).thenReturn(Optional.of(policy));

        // When
        Integer result = service.calculateRefundAmount(policyId, totalAmount, daysRemaining);

        // Then
        assertEquals(750, result);  // 75% of 1000 = 750
        verify(repository).findById(policyId);
    }

    @Test
    @DisplayName("Should return zero refund amount when not eligible")
    void calculateRefundAmount_whenNotEligible_returnsZero() {
        // Given
        Integer policyId = 1;
        Integer totalAmount = 1000;
        Integer daysRemaining = 5;

        CancellationPolicy policy = CancellationPolicy.builder()
                .id(policyId)
                .name("Test Policy")
                .refundPercentage(75) // 75% refund
                .daysBeforeTour(7)    // requires 7 days notice
                .build();

        when(repository.findById(policyId)).thenReturn(Optional.of(policy));

        // When
        Integer result = service.calculateRefundAmount(policyId, totalAmount, daysRemaining);

        // Then
        assertEquals(0, result);  // Not eligible, so 0 refund
        verify(repository).findById(policyId);
    }

    @Test
    @DisplayName("Should return all active policies")
    void getActivePolicies_returnsAllPolicies() {
        // Given
        when(repository.findAll()).thenReturn(cancellationPolicies);

        // When
        List<CancellationPolicyResponse> result = service.getActivePolicies();

        // Then
        assertNotNull(result);
        assertEquals(cancellationPolicies.size(), result.size());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should bulk create multiple policies")
    void bulkCreatePolicies_success() {
        // Given
        CancellationPolicyRequest request1 = new CancellationPolicyRequest(
                "Bulk Policy 1", "Description 1", 50, 10);
        CancellationPolicyRequest request2 = new CancellationPolicyRequest(
                "Bulk Policy 2", "Description 2", 25, 5);

        List<CancellationPolicyRequest> requests = List.of(request1, request2);

        when(repository.findByName(any())).thenReturn(Optional.empty());

        // When
        service.bulkCreatePolicies(requests);

        // Then
        verify(repository, times(2)).findByName(any());
        verify(repository).saveAll(policiesCaptor.capture());

        List<CancellationPolicy> savedPolicies = policiesCaptor.getValue();
        assertEquals(2, savedPolicies.size());
        assertEquals(request1.name(), savedPolicies.get(0).getName());
        assertEquals(request2.name(), savedPolicies.get(1).getName());
    }

    @Test
    @DisplayName("Should check if policy with name exists")
    void existsPolicyWithName_whenExists_returnsTrue() {
        // Given
        String name = "Test Policy";
        when(repository.findByName(name)).thenReturn(Optional.of(cancellationPolicy));

        // When
        boolean result = service.existsPolicyWithName(name);

        // Then
        assertTrue(result);
        verify(repository).findByName(name);
    }

    @Test
    @DisplayName("Should check if policy with name does not exist")
    void existsPolicyWithName_whenNotExists_returnsFalse() {
        // Given
        String name = "Non-existent Policy";
        when(repository.findByName(name)).thenReturn(Optional.empty());

        // When
        boolean result = service.existsPolicyWithName(name);

        // Then
        assertFalse(result);
        verify(repository).findByName(name);
    }

    @Test
    @DisplayName("Should check if policy is used by tours")
    void isPolicyUsedByAnyTour_returnsFalse() {
        // Given/When
        boolean result = service.isPolicyUsedByAnyTour(1);

        // Then - currently implemented to always return false
        assertFalse(result);
    }
}