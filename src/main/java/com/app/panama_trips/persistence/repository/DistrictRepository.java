package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    Optional<District> findDistrictByName(String name);
}
