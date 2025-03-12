package com.app.panama_trips.presentation.dto;

import java.time.LocalDateTime;

public record UserResponse(
    Long id,
    String dni,
    String name,
    String lastname,
    String email,
    String profileImageUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long createdById,
    Long updatedById,
    Long RoleId
) { }
