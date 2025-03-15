package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.ProvinceRequest;
import com.app.panama_trips.presentation.dto.ProvinceResponse;

import java.util.List;

public interface IProvinceService {
    List<ProvinceResponse> getAllProvinces();
    ProvinceResponse getProvinceById(Integer id);
    ProvinceResponse getProvinceByName(String name);
    ProvinceResponse saveProvince(ProvinceRequest provinceRequest);
    ProvinceResponse updateProvince(Integer id, ProvinceRequest provinceRequest);
    void deleteProvince(Integer id);
}
