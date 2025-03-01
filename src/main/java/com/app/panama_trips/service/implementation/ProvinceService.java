package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Province;
import com.app.panama_trips.persistence.repository.ProvinceRepository;
import com.app.panama_trips.service.interfaces.IProvinceService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProvinceService implements IProvinceService {

    private final ProvinceRepository provinceRepository;

    public ProvinceService(ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }

    private void validateProvince(Province province) {
        if (province.getName() == null || province.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Province name is required");
        }

        if(provinceRepository.findByName(province.getName()).isPresent()) {
            throw new IllegalArgumentException("Province with name " + province.getName() + " already exists");
        }
    }

    @Override
    public List<Province> getAllProvinces() {
        return this.provinceRepository.findAll();
    }

    @Override
    public Province getProvinceById(Integer id) {
        return this.provinceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Province not found with id " + id));
    }

    @Override
    public Province getProvinceByName(String name) {
        return this.provinceRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Province not found with name " + name));
    }

    @Override
    public Province saveProvince(Province province) {
        validateProvince(province);
        return this.provinceRepository.save(province);
    }

    @Override
    public Province updateProvince(Integer id, Province province) {
        return this.provinceRepository.findById(id)
                .map(existingProvince -> {
                    existingProvince.setName(province.getName());
                    validateProvince(existingProvince);
                    return this.provinceRepository.save(existingProvince);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Province not found with id " + id));
    }

    @Override
    public void deleteProvince(Integer id) {
        if(!this.provinceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Province not found with id " + id);
        }
        this.provinceRepository.deleteById(id);
    }
}
