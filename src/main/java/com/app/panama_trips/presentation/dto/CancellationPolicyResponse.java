package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.CancellationPolicy;

public record CancellationPolicyResponse(
        Integer id,
        String name,
        String description,
        Integer refundPercentage,
        Integer daysBeforeTour
) {
    public CancellationPolicyResponse(CancellationPolicy cancellationPolicy) {
        this(
                cancellationPolicy.getId(),
                cancellationPolicy.getName(),
                cancellationPolicy.getDescription(),
                cancellationPolicy.getRefundPercentage(),
                cancellationPolicy.getDaysBeforeTour()
        );
    }
}
