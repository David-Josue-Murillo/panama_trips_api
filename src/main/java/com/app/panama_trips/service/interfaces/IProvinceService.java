package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.Province;

import java.util.List;

public interface IProvinceService {
    List<Province> getAllProvinces();
    Province getProvinceById(Integer id);
    Province getProvinceByName(String name);
    Province saveProvince(Province province);
    Province updateProvince(Integer id, Province province);
    void deleteProvince(Integer id);
}
