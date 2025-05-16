package com.app.panama_trips.service.implementation;

import com.app.panama_trips.persistence.repository.CancellationPolicyRepository;
import com.app.panama_trips.presentation.dto.CancellationPolicyRequest;
import com.app.panama_trips.presentation.dto.CancellationPolicyResponse;
import com.app.panama_trips.service.interfaces.ICancellationPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CancellationPolicyService implements ICancellationPolicyService {

    private final CancellationPolicyRepository repository;

    @Override
    public Page<CancellationPolicyResponse> getAllCancellationPolicies(Pageable pageable) {
        return null;
    }

    @Override
    public CancellationPolicyResponse getCancellationPolicyById(Integer id) {
        return null;
    }

    @Override
    public CancellationPolicyResponse saveCancellationPolicy(CancellationPolicyRequest request) {
        return null;
    }

    @Override
    public CancellationPolicyResponse updateCancellationPolicy(Integer id, CancellationPolicyRequest request) {
        return null;
    }

    @Override
    public void deleteCancellationPolicy(Integer id) {

    }

    @Override
    public List<CancellationPolicyResponse> findByRefundPercentageGreaterThanEqual(Integer minRefundPercentage) {
        return List.of();
    }

    @Override
    public List<CancellationPolicyResponse> findByDaysBeforeTourGreaterThanEqual(Integer minDaysBeforeTour) {
        return List.of();
    }

    @Override
    public Optional<CancellationPolicyResponse> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public List<CancellationPolicyResponse> findEligiblePolicies(Integer minPercentage, Integer maxDays) {
        return List.of();
    }

    @Override
    public CancellationPolicyResponse getRecommendedPolicy(Integer daysBeforeTrip) {
        return null;
    }

    @Override
    public boolean isPolicyEligibleForRefund(Integer policyId, Integer daysRemaining) {
        return false;
    }

    @Override
    public Integer calculateRefundAmount(Integer policyId, Integer totalAmount, Integer daysRemaining) {
        return 0;
    }

    @Override
    public List<CancellationPolicyResponse> getActivePolicies() {
        return List.of();
    }

    @Override
    public void bulkCreatePolicies(List<CancellationPolicyRequest> requests) {

    }

    @Override
    public void bulkUpdatePolicies(List<CancellationPolicyRequest> requests) {

    }

    @Override
    public boolean existsPolicyWithName(String name) {
        return false;
    }

    @Override
    public boolean isPolicyUsedByAnyTour(Integer policyId) {
        return false;
    }
}
