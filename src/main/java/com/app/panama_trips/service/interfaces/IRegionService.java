package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.RegionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRegionService {
    // Crud operations
    Page<RegionResponse> getAllRegions(Pageable pageable);
    RegionResponse getRegionById(Integer id);
    RegionResponse saveRegion(RegionResponse regionResponse);
    RegionResponse updateRegion(Integer id, RegionResponse regionResponse);
    void deleteRegion(Integer id);

    // Additional service methods
    RegionResponse getRegionByName(String name);
    Page<RegionResponse> getRegionsByName(String name, Pageable pageable);
    Page<RegionResponse> getRegionByProvinceId(Integer provinceId);
    Page<RegionResponse> getRegionByComarcaId(Integer comarcaId);
    boolean existsRegionByName(String name);
}
