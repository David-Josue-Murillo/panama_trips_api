package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.Provider;

public record ProviderResponse(
        Integer id,
        String ruc,
        String name,
        String email,
        String phone,
        Integer provinceId,
        Integer districtId,
        Integer streetId,
        String createdAt
) {
    public ProviderResponse(Provider provider) {
        this(
                provider.getId(),
                provider.getRuc(),
                provider.getName(),
                provider.getEmail(),
                provider.getPhone(),
                provider.getProvince().getId(),
                provider.getDistrict().getId(),
                provider.getStreet().getAddressId(),
                provider.getCreatedAt().toString()
        );
    }
}
