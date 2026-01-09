package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.Language;

public record LanguageResponse(
        String code,
        String name,
        Boolean isActive) {

    public LanguageResponse(Language language) {
        this(
                language.getCode(),
                language.getName(),
                language.getIsActive()
            );
    }
}
