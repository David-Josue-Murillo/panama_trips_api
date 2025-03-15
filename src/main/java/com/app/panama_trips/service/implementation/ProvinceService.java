package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Province;
import com.app.panama_trips.persistence.repository.ProvinceRepository;
import com.app.panama_trips.presentation.dto.ProvinceRequest;
import com.app.panama_trips.presentation.dto.ProvinceResponse;
import com.app.panama_trips.service.interfaces.IProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinceService implements IProvinceService {

    private final ProvinceRepository provinceRepository;

    private void validateProvince(String provinceName) {
        if(provinceRepository.findByName(provinceName).isPresent()) {
            throw new IllegalArgumentException("Province with name " + provinceName + " already exists");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProvinceResponse> getAllProvinces() {
        return this.provinceRepository.findAll().stream().map(this::convertToResponseDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProvinceResponse getProvinceById(Integer id) {
        return this.provinceRepository.findById(id)
                .map(this::convertToResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Province not found with id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public ProvinceResponse getProvinceByName(String name) {
        return this.provinceRepository.findByName(name)
                .map(this::convertToResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Province not found with name " + name));
    }

    @Override
    @Transactional
    public ProvinceResponse saveProvince(ProvinceRequest provinceRequest) {
        this.validateProvince(provinceRequest.name());
        Province province = new Province();
        province.setName(provinceRequest.name());
        return convertToResponseDTO(this.provinceRepository.save(province));
    }

    @Override
    @Transactional
    public ProvinceResponse updateProvince(Integer id, ProvinceRequest provinceRequest) {
        Province provinceExisting = this.provinceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Province not found with id " + id));
        validateProvince(provinceRequest.name());
        provinceExisting.setName(provinceRequest.name());
        return convertToResponseDTO(this.provinceRepository.save(provinceExisting));
    }

    @Override
    @Transactional
    public void deleteProvince(Integer id) {
        if(this.provinceRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Province not found with id " + id);
        }
        this.provinceRepository.deleteById(id);
    }

    private ProvinceResponse convertToResponseDTO(Province province) {
        return new ProvinceResponse(province.getId(), province.getName());
    }
}
