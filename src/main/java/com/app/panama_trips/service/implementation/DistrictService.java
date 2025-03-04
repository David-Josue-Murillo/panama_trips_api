package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.District;
import com.app.panama_trips.persistence.entity.Province;
import com.app.panama_trips.persistence.repository.DistrictRepository;
import com.app.panama_trips.persistence.repository.ProvinceRepository;
import com.app.panama_trips.presentation.dto.DistrictRequest;
import com.app.panama_trips.service.interfaces.IDistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DistrictService implements IDistrictService {

    private final DistrictRepository districtRepository;
    private final ProvinceRepository provinceRepository;

    private void validateDistricts(DistrictRequest districtRequest) {
        if (districtRepository.findByNameAndProvinceId_Id(districtRequest.name(), districtRequest.province()).isPresent()) {
            throw new IllegalArgumentException("District with name " + districtRequest.name() + " already exists in the province");
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
    public District saveDistrict(DistrictRequest districtRequest) {
        this.validateDistricts(districtRequest);
        System.out.println("districtRequest.provinceId() = " + districtRequest.province());
        Province province = this.provinceRepository.findById(districtRequest.province())
                .orElseThrow(() -> new ResourceNotFoundException("Province not found with id " + districtRequest.province()));

        District district = new District();
        district.setName(districtRequest.name());
        district.setProvince(province);
        return this.districtRepository.save(district);
    }

    @Override
    @Transactional
    public District updateDistrict(Integer id, DistrictRequest districtRequest) {
        District existingDistrict = this.getDistrictById(id);
        Province province = this.provinceRepository.findById(districtRequest.province())
                .orElseThrow(() -> new ResourceNotFoundException("Province not found with id " + districtRequest.province()));

        existingDistrict.setName(districtRequest.name());
        existingDistrict.setProvince(province);
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
