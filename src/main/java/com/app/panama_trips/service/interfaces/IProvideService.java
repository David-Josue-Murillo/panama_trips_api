package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.ProviderRequest;
import com.app.panama_trips.presentation.dto.ProviderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProvideService {
    // CRUD operations
    Page<ProviderResponse> getAllProviders(Pageable pageable);
    ProviderResponse getProviderById(Integer id);
    ProviderResponse saveProvider(ProviderRequest provider);
    ProviderResponse updateProvider(Integer id, ProviderRequest provider);
    void deleteProvider(Integer id);

    // Additional service methods
    ProviderResponse getProviderByRuc(String ruc);
    ProviderResponse getProviderByName(String name);
    ProviderResponse getProviderByEmail(String email);
    ProviderResponse getProviderByPhone(String phone);
    List<ProviderResponse> getProvidersByProvinceId(Integer provinceId);
    List<ProviderResponse> getProvidersByDistrictId(Integer districtId);
    List<ProviderResponse> getProvidersByAddressId(Integer addressId);
    List<ProviderResponse> getProvidersByNameFragment(String nameFragment);
    boolean existsProviderByEmail(String email);
    boolean existsProviderByRuc(String ruc);
    long countProviders();
}
