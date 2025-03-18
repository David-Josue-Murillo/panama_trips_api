package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.Province;

public record ProvinceResponse(Integer id, String name) {
    public ProvinceResponse(Province province) {
        this(province.getId(), province.getName());
    }
}
