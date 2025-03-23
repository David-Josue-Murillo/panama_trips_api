package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Comarca;
import com.app.panama_trips.persistence.entity.Province;
import com.app.panama_trips.persistence.entity.Region;
import com.app.panama_trips.persistence.repository.ComarcaRepository;
import com.app.panama_trips.persistence.repository.ProvinceRepository;
import com.app.panama_trips.persistence.repository.RegionRepository;
import com.app.panama_trips.presentation.dto.RegionRequest;
import com.app.panama_trips.presentation.dto.RegionResponse;
import com.app.panama_trips.service.interfaces.IRegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegionService implements IRegionService {

    private final RegionRepository regionRepository;
    private final ProvinceRepository provinceRepository;
    private final ComarcaRepository comarcaRepository;

    @Override
    public Page<RegionResponse> getAllRegions(Pageable pageable) {
        return this.regionRepository.findAll(pageable).map(RegionResponse::new);
    }

    @Override
    public RegionResponse getRegionById(Integer id) {
        return this.regionRepository.findById(id)
                .map(RegionResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("Region with id " + id + " not found"));
    }

    @Override
    public RegionResponse saveRegion(RegionRequest regionRequest) {
        validateRegion(regionRequest);
        Region newRegion = builderRegionFromRequest(regionRequest);
        return new RegionResponse(this.regionRepository.save(newRegion));
    }

    @Override
    public RegionResponse updateRegion(Integer id, RegionRequest regionRequest) {
        validateRegion(regionRequest);
        Region region = this.regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region with id " + id + " not found"));
        updateRegionFromRequest(region, regionRequest);
        return new RegionResponse(this.regionRepository.save(region));
    }

    @Override
    public void deleteRegion(Integer id) {
        if(!this.regionRepository.existsById(id)) {
            throw  new ResourceNotFoundException("Region with id " + id + " not found");
        }
        this.regionRepository.deleteById(id);
    }

    @Override
    public RegionResponse getRegionByName(String name) {
        return this.regionRepository.findByNameIgnoreCase(name)
                .map(RegionResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("Region with name " + name + " not found"));
    }

    @Override
    public Page<RegionResponse> getRegionsByName(String name, Pageable pageable) {
        return this.regionRepository.findByNameContainingIgnoreCase(name, pageable).map(RegionResponse::new);
    }

    @Override
    public Page<RegionResponse> getRegionByProvinceId(Integer provinceId, Pageable pageable) {
        return this.regionRepository.findByProvince_Id(provinceId, pageable).map(RegionResponse::new);
    }

    @Override
    public Page<RegionResponse> getRegionByComarcaId(Integer comarcaId, Pageable pageable) {
        return this.regionRepository.findByComarca_Id(comarcaId, pageable).map(RegionResponse::new);
    }

    @Override
    public boolean existsRegionByName(String name) {
        return this.regionRepository.existsByNameIgnoreCase(name);
    }

    // Private methods
    private void validateRegion(RegionRequest request) {
        if (existsRegionByName(request.name())) {
            throw new IllegalArgumentException("Region with name " + request.name() + " already exists");
        }

        if (!this.provinceRepository.existsById(request.provinceId())) {
            throw new IllegalArgumentException("Province with id " + request.provinceId() + " not found");
        }

        if (!this.comarcaRepository.existsById(request.comarcaId())) {
            throw new IllegalArgumentException("Comarca with id " + request.comarcaId() + " not found");
        }
    }

    private Region builderRegionFromRequest(RegionRequest request) {
        return Region.builder()
                .name(request.name())
                .province(findProvinceOrFail(request.provinceId()))
                .comarca(findComarcaOrFail(request.comarcaId()))
                .build();
    }

    private void updateRegionFromRequest(Region region, RegionRequest request) {
        region.setName(request.name());
        region.setProvince(findProvinceOrFail(request.provinceId()));
        region.setComarca(findComarcaOrFail(request.comarcaId()));
    }

    private Province findProvinceOrFail(Integer provinceId) {
        return this.provinceRepository.findById(provinceId)
                .orElseThrow(() -> new IllegalArgumentException("Province with id " + provinceId + " not found"));
    }

    private Comarca findComarcaOrFail(Integer comarcaId) {
        return this.comarcaRepository.findById(comarcaId)
                .orElseThrow(() -> new IllegalArgumentException("Comarca with id " + comarcaId + " not found"));
    }
}
