package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.Province;
import com.app.panama_trips.presentation.dto.ProvinceRequest;

import java.util.List;

public interface IProvinceService {
    List<Province> getAllProvinces();
    Province getProvinceById(Integer id);
    Province getProvinceByName(String name);
    Province saveProvince(ProvinceRequest provinceRequest);
    Province updateProvince(Integer id, ProvinceRequest provinceRequest);
    void deleteProvince(Integer id);
}
