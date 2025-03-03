package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.District;

import java.util.List;

public interface IDistrictService {
    List<District> getAllDistricts();
    District getDistrictById(Integer id);
    District getDistrictByName(String name);
    List<District> getDistrictsByProvinceId(Integer provinceId);
    District saveDistrict(District district);
    District updateDistrict(Integer id, District district);
    void deleteDistrict(Integer id);
}
