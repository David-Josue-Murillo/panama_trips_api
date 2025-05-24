package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.Guide;

public record GuideResponse(
    Integer id,
    Long userId,
    String nameProvider,
    String biografy,
    String spelcialties,
    String languages,
    Integer yearExperience,
    String certificationDetails,
    Boolean isActive
) {
    public GuideResponse(Guide guide) {
        this(
            guide.getId(),
            guide.getUser().getId(),
            guide.getProvider().getName(),
            guide.getBio(),
            guide.getSpecialties(),
            guide.getLanguages(),
            guide.getYearsExperience(),
            guide.getCertificationDetails(),
            guide.getIsActive()
        );
    }
}
