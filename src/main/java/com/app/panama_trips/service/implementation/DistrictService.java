package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.District;
import com.app.panama_trips.persistence.repository.DistrictRepository;
import com.app.panama_trips.service.interfaces.IDistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DistrictService implements IDistrictService {

    private final DistrictRepository districtRepository;

    private void validateDistricts(District district) {
        if (district.getName() == null || district.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("District name is required");
        }

        if (district.getProvince() == null) {
            throw new IllegalArgumentException("Province is required");
        }

        if (districtRepository.findByNameAndProvinceId_Id(district.getName(), district.getProvince().getId()).isPresent()) {
            throw new IllegalArgumentException("District with name " + district.getName() + " already exists in the province");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<District> getAllDistricts() {
        return this.districtRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public District getDistrictById(Integer id) {
        return this.districtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("District not found with id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public District getDistrictByName(String name) {
        return this.districtRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("District not found with name " + name));
    }

    @Override
    @Transactional(readOnly = true)
    public List<District> getDistrictsByProvinceId(Integer provinceId) {
        return this.districtRepository.findDistrictByProvinceId_Id(provinceId);
    }

    @Override
    @Transactional
    public District saveDistrict(District district) {
        this.validateDistricts(district);
        return this.districtRepository.save(district);
    }

    @Override
    @Transactional
    public District updateDistrict(Integer id, District district) {
        District existingDistrict = this.getDistrictById(id);
        existingDistrict.setName(district.getName());
        existingDistrict.setProvince(district.getProvince());
        return this.districtRepository.save(existingDistrict);
    }

    @Override
    @Transactional
    public void deleteDistrict(Integer id) {
        if(!this.districtRepository.existsById(id)) {
            throw new ResourceNotFoundException("District not found with id " + id);
        }
        this.districtRepository.deleteById(id);
    }
}
