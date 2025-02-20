package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserEntitiesByName(String name);
    boolean findUserEntitiesByEmail(String email);
}
