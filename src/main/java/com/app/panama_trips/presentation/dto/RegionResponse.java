package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.Region;

public record RegionResponse(
        Integer id,
        String name,
        String provinceName,
        Integer provinceId,
        String comarcaName,
        Integer comarcaId
) {
    public RegionResponse(Region region) {
        this(
                region.getId(),
                region.getName(),
                region.getProvince() != null ? region.getProvince().getName() : null,
                region.getProvince() != null ? region.getProvince().getId() : null,
                region.getComarca() != null ? region.getComarca().getName() : null,
                region.getComarca() != null ? region.getComarca().getId() : null
        );
    }
}