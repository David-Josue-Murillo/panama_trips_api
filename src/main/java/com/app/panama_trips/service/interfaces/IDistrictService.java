package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.DistrictRequest;
import com.app.panama_trips.presentation.dto.DistrictResponse;

import java.util.List;

public interface IDistrictService {
    List<DistrictResponse> getAllDistricts();
    DistrictResponse getDistrictById(Integer id);
    DistrictResponse getDistrictByName(String name);
    List<DistrictResponse> getDistrictsByProvinceId(Integer provinceId);
    DistrictResponse saveDistrict(DistrictRequest districtRequest);
    DistrictResponse updateDistrict(Integer id, DistrictRequest districtRequest);
    void deleteDistrict(Integer id);
}
