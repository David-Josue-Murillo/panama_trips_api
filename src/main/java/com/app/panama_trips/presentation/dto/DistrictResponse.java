package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.District;

public record DistrictResponse(Integer id, String name, Integer provinceId) {
    public DistrictResponse(District district) {
        this(
                district.getId(),
                district.getName(),
                district.getProvince().getId()
        );
    }
}
