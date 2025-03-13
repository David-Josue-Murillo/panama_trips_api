package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Street;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StreetRepository extends JpaRepository<Street, Integer> {
    List<Street> findByDistrictId_Id(Integer districtIdId);
    Optional<Street> findByName(String name);
    boolean existsByName(String name);
}
