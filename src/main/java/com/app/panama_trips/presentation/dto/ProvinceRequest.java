package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record ProvinceRequest(@NotBlank String name) { }
