package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserEntitiesByName(String name);
    Optional<UserEntity> findUserEntitiesByEmail(String email);
}
