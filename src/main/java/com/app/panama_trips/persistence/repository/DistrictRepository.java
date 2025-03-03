package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.District;
import com.app.panama_trips.persistence.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    Optional<District> findByName(String name);
    List<District> findDistrictByProvinceId_Id(Integer provinceIdId);
}
