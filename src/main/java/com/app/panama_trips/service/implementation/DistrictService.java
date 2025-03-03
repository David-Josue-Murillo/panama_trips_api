package com.app.panama_trips.service.implementation;

import com.app.panama_trips.persistence.entity.District;
import com.app.panama_trips.service.interfaces.IDistrictService;

import java.util.List;

public class DistrictService implements IDistrictService {
    @Override
    public List<District> getAllDistricts() {
        return List.of();
    }

    @Override
    public District getDistrictById(Integer id) {
        return null;
    }

    @Override
    public District getDistrictByName(String name) {
        return null;
    }

    @Override
    public District saveDistrict(District district) {
        return null;
    }

    @Override
    public District updateDistrict(Integer id, District district) {
        return null;
    }

    @Override
    public void deleteDistrict(Long id) {

    }
}
