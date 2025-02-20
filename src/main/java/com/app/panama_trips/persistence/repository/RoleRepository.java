package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    RoleEntity findByRoleEnumIn(String roleNames);
}
