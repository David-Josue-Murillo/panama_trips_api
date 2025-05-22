package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.CancellationPolicyRequest;
import com.app.panama_trips.presentation.dto.CancellationPolicyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ICancellationPolicyService {
    // CRUD operations
    Page<CancellationPolicyResponse> getAllCancellationPolicies(Pageable pageable);
    CancellationPolicyResponse getCancellationPolicyById(Integer id);
    CancellationPolicyResponse saveCancellationPolicy(CancellationPolicyRequest request);
    CancellationPolicyResponse updateCancellationPolicy(Integer id, CancellationPolicyRequest request);
    void deleteCancellationPolicy(Integer id);

    // Find operations
    List<CancellationPolicyResponse> findByRefundPercentageGreaterThanEqual(Integer minRefundPercentage);
    List<CancellationPolicyResponse> findByDaysBeforeTourGreaterThanEqual(Integer minDaysBeforeTour);
    Optional<CancellationPolicyResponse> findByName(String name);
    List<CancellationPolicyResponse> findEligiblePolicies(Integer minPercentage, Integer maxDays);

    // Specialized queries
    CancellationPolicyResponse getRecommendedPolicy(Integer daysBeforeTrip);
    boolean isPolicyEligibleForRefund(Integer policyId, Integer daysRemaining);
    Integer calculateRefundAmount(Integer policyId, Integer totalAmount, Integer daysRemaining);
    List<CancellationPolicyResponse> getActivePolicies();

    // Bulk operations
    void bulkCreatePolicies(List<CancellationPolicyRequest> requests);
    void bulkUpdatePolicies(List<CancellationPolicyRequest> requests);

    // Check operations
    boolean existsPolicyWithName(String name);
    boolean isPolicyUsedByAnyTour(Integer policyId);
}