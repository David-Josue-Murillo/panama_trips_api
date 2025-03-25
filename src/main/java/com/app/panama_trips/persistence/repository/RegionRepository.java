package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {
    Optional<Region> findByNameIgnoreCase(String name);
    Page<Region> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Region> findByProvince_Id(Integer provinceId, Pageable pageable);
    Page<Region> findByComarca_Id(Integer comarcaId, Pageable pageable);
    boolean existsByNameIgnoreCase(String name);
}
