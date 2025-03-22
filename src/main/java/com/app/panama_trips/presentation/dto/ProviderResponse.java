package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.Provider;

public record ProviderResponse(
        Integer id,
        String ruc,
        String name,
        String email,
        String phone,
        String province,
        String district,
        String street,
        String createdAt
) {
    public ProviderResponse(Provider provider) {
        this(
                provider.getId(),
                provider.getRuc(),
                provider.getName(),
                provider.getEmail(),
                provider.getPhone(),
                provider.getProvince().getName(),
                provider.getDistrict().getName(),
                provider.getAddress().getStreet(),
                provider.getCreatedAt().toString()
        );
    }
}
