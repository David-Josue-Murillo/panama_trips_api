package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Region;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {
    Optional<Region> findByNameIgnoreCase(String name);
    List<Region> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Region> findByProvince_Id(Integer provinceId);
    List<Region> findByComarca_Id(Integer comarcaId);
    boolean existsByNameIgnoreCase(String name);
}
