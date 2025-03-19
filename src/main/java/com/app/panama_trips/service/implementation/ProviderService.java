package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Address;
import com.app.panama_trips.persistence.entity.District;
import com.app.panama_trips.persistence.entity.Provider;
import com.app.panama_trips.persistence.entity.Province;
import com.app.panama_trips.persistence.repository.AddressRepository;
import com.app.panama_trips.persistence.repository.DistrictRepository;
import com.app.panama_trips.persistence.repository.ProviderRepository;
import com.app.panama_trips.persistence.repository.ProvinceRepository;
import com.app.panama_trips.presentation.dto.ProviderRequest;
import com.app.panama_trips.presentation.dto.ProviderResponse;
import com.app.panama_trips.service.interfaces.IProvideService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderService implements IProvideService {

    private final ProviderRepository providerRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final AddressRepository addressRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ProviderResponse> getAllProviders(Pageable pageable) {
        return this.providerRepository.findAll(pageable)
                .map(ProviderResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderResponse getProviderById(Integer id) {
        return this.providerRepository.findById(id)
                .map(ProviderResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("Provider with " + id + " not found"));
    }

    @Override
    @Transactional
    public ProviderResponse saveProvider(ProviderRequest provider) {
        validateProvider(provider);
        Provider newProvider = builderProviderFromRequest(provider);
        return new ProviderResponse(this.providerRepository.save(newProvider));
    }

    @Override
    @Transactional
    public ProviderResponse updateProvider(Integer id, ProviderRequest provider) {
        Provider providerExisting = this.providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider with " + id + " not found"));
        updateProviderFields(providerExisting, provider);
        return new ProviderResponse(this.providerRepository.save(providerExisting));
    }

    @Override
    @Transactional
    public void deleteProvider(Integer id) {
        if(!this.providerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Provider with " + id + " not found");
        }
        this.providerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderResponse getProviderByRuc(String ruc) {
        return new ProviderResponse(this.providerRepository.findByRuc(ruc)
                .orElseThrow(() -> new ResourceNotFoundException("Provider with " + ruc + " not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderResponse getProviderByName(String name) {
        return new ProviderResponse(this.providerRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Provider with " + name + " not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderResponse getProviderByEmail(String email) {
        return new ProviderResponse(this.providerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Provider with " + email + " not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderResponse getProviderByPhone(String phone) {
        return new ProviderResponse(this.providerRepository.findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException("Provider with " + phone + " not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProviderResponse> getProvidersByProvinceId(Integer provinceId) {
        return this.providerRepository.findProvidersByProvince_Id(provinceId)
                .stream()
                .map(ProviderResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProviderResponse> getProvidersByDistrictId(Integer districtId) {
        return this.providerRepository.findProvidersByDistrict_Id(districtId)
                .stream()
                .map(ProviderResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProviderResponse> getProvidersByAddressId(Integer addressId) {
        return this.providerRepository.findProvidersByAddress_AddressId(addressId)
                .stream()
                .map(ProviderResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProviderResponse> getProvidersByNameFragment(String nameFragment) {
        return this.providerRepository.findProvidersByNameContainingIgnoreCase(nameFragment)
                .stream()
                .map(ProviderResponse::new)
                .toList();
    }

    @Override
    public boolean existsProviderByEmail(String email) {
        return this.providerRepository.existsProviderByEmail(email);
    }

    @Override
    public boolean existsProviderByRuc(String ruc) {
        return this.providerRepository.existsProviderByRuc(ruc);
    }

    @Override
    public long countProviders() {
        return this.providerRepository.count();
    }

    // Methods Private
    private void validateProvider(ProviderRequest provider) {
        if (existsProviderByRuc(provider.ruc())) {
            throw new IllegalArgumentException("Provider with RUC " + provider.ruc() + " already exists");
        }
        if (existsProviderByEmail(provider.email())) {
            throw new IllegalArgumentException("Provider with email " + provider.email() + " already exists");
        }
    }

    private Provider builderProviderFromRequest(ProviderRequest request) {
        return Provider.builder()
                .ruc(request.ruc())
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .province(findProvinceOrFail(request.provinceId()))
                .district(findDistrictOrFail(request.districtId()))
                .address(findAddressOrFail(request.addressId()))
                .build();
    }

    private Province findProvinceOrFail(Integer provinceId) {
        return this.provinceRepository.findById(provinceId)
                .orElseThrow(() -> new ResourceNotFoundException("Province with id " + provinceId + " not found"));
    }

    private District findDistrictOrFail(Integer districtId) {
        return this.districtRepository.findById(districtId)
                .orElseThrow(() -> new ResourceNotFoundException("District with id " + districtId + " not found"));
    }

    private Address findAddressOrFail(Integer addressId) {
        return this.addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address with id " + addressId + " not found"));
    }

    private void updateProviderFields(Provider existingProvider, ProviderRequest provider) {
        existingProvider.setRuc(provider.ruc());
        existingProvider.setName(provider.name());
        existingProvider.setEmail(provider.email());
        existingProvider.setPhone(provider.phone());
        existingProvider.setProvince(findProvinceOrFail(provider.provinceId()));
        existingProvider.setDistrict(findDistrictOrFail(provider.districtId()));
        existingProvider.setAddress(findAddressOrFail(provider.addressId()));
    }
}
