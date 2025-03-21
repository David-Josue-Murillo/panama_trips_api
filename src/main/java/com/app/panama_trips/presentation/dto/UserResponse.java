package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.UserEntity;

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
    UserEntity createdById,
    UserEntity updatedById,
    Integer RoleId
) {
    public UserResponse(UserEntity userEntity) {
        this(
            userEntity.getId(),
            userEntity.getDni(),
            userEntity.getName(),
            userEntity.getLastname(),
            userEntity.getEmail(),
            userEntity.getProfileImageUrl(),
            userEntity.getCreatedAt(),
            userEntity.getUpdatedAt(),
            userEntity.getCreatedBy(),
            userEntity.getUpdatedBy(),
            userEntity.getRole_id().getId()
        );
    }
}
