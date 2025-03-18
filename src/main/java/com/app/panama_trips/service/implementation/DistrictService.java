package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.District;
import com.app.panama_trips.persistence.entity.Province;
import com.app.panama_trips.persistence.repository.DistrictRepository;
import com.app.panama_trips.persistence.repository.ProvinceRepository;
import com.app.panama_trips.presentation.dto.DistrictRequest;
import com.app.panama_trips.presentation.dto.DistrictResponse;
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


    @Override
    @Transactional(readOnly = true)
    public List<DistrictResponse> getAllDistricts() {
        return this.districtRepository.findAll().stream().map(DistrictResponse::new).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DistrictResponse getDistrictById(Integer id) {
        return this.districtRepository.findById(id)
                .map(DistrictResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("District not found with id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public DistrictResponse getDistrictByName(String name) {
        return this.districtRepository.findByName(name)
                .map(DistrictResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("District not found with name " + name));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DistrictResponse> getDistrictsByProvinceId(Integer provinceId) {
        List<District> districts = this.districtRepository.findDistrictByProvinceId_Id(provinceId);
        return districts.isEmpty() ? null : districts.stream().map(DistrictResponse::new).toList();
    }

    @Override
    @Transactional
    public DistrictResponse saveDistrict(DistrictRequest districtRequest) {
        validateDistricts(districtRequest);
        District district = builderDistrictFromRequest(districtRequest);
        return new DistrictResponse(this.districtRepository.save(district));
    }

    @Override
    @Transactional
    public DistrictResponse updateDistrict(Integer id, DistrictRequest districtRequest) {
        District existingDistrict = this.districtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("District not found with id " + id));

        updateDistrictFields(existingDistrict, districtRequest);
        return new DistrictResponse(this.districtRepository.save(existingDistrict));
    }

    @Override
    @Transactional
    public void deleteDistrict(Integer id) {
        if(!this.districtRepository.existsById(id)) {
            throw new ResourceNotFoundException("District not found with id " + id);
        }
        this.districtRepository.deleteById(id);
    }

    // Private methods
    private void validateDistricts(DistrictRequest districtRequest) {
        if (districtRepository.findByNameAndProvinceId_Id(districtRequest.name(), districtRequest.provinceId()).isPresent()) {
            throw new IllegalArgumentException("District with name " + districtRequest.name() + " already exists in the province");
        }
    }

    private District builderDistrictFromRequest(DistrictRequest request) {
        return District.builder()
                .name(request.name())
                .province(findProvinceOrFail(request.provinceId()))
                .build();
    }

    private Province findProvinceOrFail(Integer provinceId) {
        return this.provinceRepository.findById(provinceId)
                .orElseThrow(() -> new ResourceNotFoundException("Province with id " + provinceId + " not found"));
    }

    private void updateDistrictFields(District district, DistrictRequest request) {
        district.setName(request.name());
        district.setProvince(findProvinceOrFail(request.provinceId()));
    }
}
