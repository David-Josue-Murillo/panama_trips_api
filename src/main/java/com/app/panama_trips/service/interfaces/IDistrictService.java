package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.District;
import com.app.panama_trips.presentation.dto.DistrictRequest;

import java.util.List;

public interface IDistrictService {
    List<District> getAllDistricts();
    District getDistrictById(Integer id);
    District getDistrictByName(String name);
    List<District> getDistrictsByProvinceId(Integer provinceId);
    District saveDistrict(DistrictRequest districtRequest);
    District updateDistrict(Integer id, DistrictRequest districtRequest);
    void deleteDistrict(Integer id);
}
