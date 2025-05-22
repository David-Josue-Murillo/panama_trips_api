package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.CancellationPolicy;
import com.app.panama_trips.persistence.repository.CancellationPolicyRepository;
import com.app.panama_trips.presentation.dto.CancellationPolicyRequest;
import com.app.panama_trips.presentation.dto.CancellationPolicyResponse;
import com.app.panama_trips.service.interfaces.ICancellationPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CancellationPolicyService implements ICancellationPolicyService {

    private final CancellationPolicyRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<CancellationPolicyResponse> getAllCancellationPolicies(Pageable pageable) {
        return repository.findAll(pageable)
                .map(CancellationPolicyResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public CancellationPolicyResponse getCancellationPolicyById(Integer id) {
        return repository.findById(id)
                .map(CancellationPolicyResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("Cancellation Policy not found with id: " + id));
    }

    @Override
    @Transactional
    public CancellationPolicyResponse saveCancellationPolicy(CancellationPolicyRequest request) {
        validateCancellationPolicy(request, null);
        CancellationPolicy policy = buildFromRequest(request);
        return new CancellationPolicyResponse(repository.save(policy));
    }

    @Override
    @Transactional
    public CancellationPolicyResponse updateCancellationPolicy(Integer id, CancellationPolicyRequest request) {
        validateCancellationPolicy(request, id);
        CancellationPolicy policy = findCancellationPolicyOrFail(id);
        updateFromRequest(policy, request);
        return new CancellationPolicyResponse(repository.save(policy));
    }

    @Override
    @Transactional
    public void deleteCancellationPolicy(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Cancellation Policy not found with id: " + id);
        }

        // Could add check to prevent deletion if policy is in use
        if (isPolicyUsedByAnyTour(id)) {
            throw new IllegalArgumentException("Cannot delete a cancellation policy that is being used by tours");
        }

        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CancellationPolicyResponse> findByRefundPercentageGreaterThanEqual(Integer minRefundPercentage) {
        return repository.findByRefundPercentageGreaterThanEqual(minRefundPercentage)
                .stream()
                .map(CancellationPolicyResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CancellationPolicyResponse> findByDaysBeforeTourGreaterThanEqual(Integer minDaysBeforeTour) {
        return repository.findByDaysBeforeTourGreaterThanEqual(minDaysBeforeTour)
                .stream()
                .map(CancellationPolicyResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CancellationPolicyResponse> findByName(String name) {
        return repository.findByName(name)
                .map(CancellationPolicyResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CancellationPolicyResponse> findEligiblePolicies(Integer minPercentage, Integer maxDays) {
        return repository.findEligiblePolicies(minPercentage, maxDays)
                .stream()
                .map(CancellationPolicyResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CancellationPolicyResponse getRecommendedPolicy(Integer daysBeforeTrip) {
        // Find the policy that offers the highest refund percentage for the given days
        return repository.findByDaysBeforeTourGreaterThanEqual(daysBeforeTrip)
                .stream()
                .max(Comparator.comparing(CancellationPolicy::getRefundPercentage))
                .map(CancellationPolicyResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("No eligible cancellation policy found for " + daysBeforeTrip + " days before tour"));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isPolicyEligibleForRefund(Integer policyId, Integer daysRemaining) {
        CancellationPolicy policy = findCancellationPolicyOrFail(policyId);
        return daysRemaining >= policy.getDaysBeforeTour();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer calculateRefundAmount(Integer policyId, Integer totalAmount, Integer daysRemaining) {
        CancellationPolicy policy = findCancellationPolicyOrFail(policyId);

        // If days remaining is less than policy requirement, no refund
        if (daysRemaining < policy.getDaysBeforeTour()) {
            return 0;
        }

        // Calculate refund based on percentage
        return (totalAmount * policy.getRefundPercentage()) / 100;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CancellationPolicyResponse> getActivePolicies() {
        // In this context, all policies are considered active
        // Could be extended with an 'active' flag in the entity if needed
        return repository.findAll()
                .stream()
                .map(CancellationPolicyResponse::new)
                .toList();
    }

    @Override
    @Transactional
    public void bulkCreatePolicies(List<CancellationPolicyRequest> requests) {
        List<CancellationPolicy> policies = requests.stream()
                .peek(request -> validateCancellationPolicy(request, null))
                .map(this::buildFromRequest)
                .toList();

        repository.saveAll(policies);
    }

    @Override
    @Transactional
    public void bulkUpdatePolicies(List<CancellationPolicyRequest> requests) {
        for (CancellationPolicyRequest request : requests) {
            // Assuming each request has an id field for updating
            // This would require extending the DTO or having a different DTO for updates
            if (request.name() != null) {
                Optional<CancellationPolicy> existingPolicy = repository.findByName(request.name());
                if (existingPolicy.isPresent()) {
                    Integer id = existingPolicy.get().getId();
                    validateCancellationPolicy(request, id);
                    updateFromRequest(existingPolicy.get(), request);
                    repository.save(existingPolicy.get());
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsPolicyWithName(String name) {
        return repository.findByName(name).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isPolicyUsedByAnyTour(Integer policyId) {
        // This would typically check for relationships with tour plans
        // For now, assuming there's no direct repository method
        // In a real implementation, you might have a specific query to check this

        // Mock implementation - replace with actual check
        return false;
    }

    // Private methods
    private void validateCancellationPolicy(CancellationPolicyRequest request, Integer id) {
        // Check for duplicate names (excluding the current policy being updated)
        Optional<CancellationPolicy> existingPolicy = repository.findByName(request.name());
        if (existingPolicy.isPresent() && (id == null || !existingPolicy.get().getId().equals(id))) {
            throw new IllegalArgumentException("A cancellation policy with this name already exists");
        }

        // Business validation: Higher refund percentages should generally have higher days before tour
        // This is a soft validation that could warn rather than error
        if (request.refundPercentage() > 75 && request.daysBeforeTour() < 7) {
            throw new IllegalArgumentException("High refund percentages (>75%) should typically require at least 7 days notice");
        }
    }

    private CancellationPolicy findCancellationPolicyOrFail(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cancellation policy not found with ID: " + id));
    }
    private CancellationPolicy buildFromRequest(CancellationPolicyRequest request) {
        return CancellationPolicy.builder()
                .name(request.name())
                .description(request.description())
                .refundPercentage(request.refundPercentage())
                .daysBeforeTour(request.daysBeforeTour())
                .build();
    }

    private void updateFromRequest(CancellationPolicy cancellationPolicy, CancellationPolicyRequest request) {
        cancellationPolicy.setName(request.name());
        cancellationPolicy.setDescription(request.description());
        cancellationPolicy.setRefundPercentage(request.refundPercentage());
        cancellationPolicy.setDaysBeforeTour(request.daysBeforeTour());
    }
}
