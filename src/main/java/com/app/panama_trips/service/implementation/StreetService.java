package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.exception.ValidationException;
import com.app.panama_trips.persistence.entity.Street;
import com.app.panama_trips.persistence.repository.DistrictRepository;
import com.app.panama_trips.persistence.repository.StreetRepository;
import com.app.panama_trips.presentation.dto.StreetRequest;
import com.app.panama_trips.presentation.dto.StreetResponse;
import com.app.panama_trips.service.interfaces.IStreetService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StreetService implements IStreetService {

    private final StreetRepository streetRepository;
    private final DistrictRepository districtRepository;

    void validateStreetName(String name) {
        if (this.streetRepository.existsByName(name)) {
            throw new ValidationException("Street with name " + name + " already exists");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StreetResponse> getAllStreet(Integer page, Integer size, Boolean enabledPagination) {
        Pageable pageable = enabledPagination
                ? PageRequest.of(page, size)
                : Pageable.unpaged();

        return this.streetRepository.findAll(pageable).map(this::convertToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StreetResponse> getAllStreetByDistrictId(Integer districtId) {
        return this.streetRepository.findByDistrictId_Id(districtId)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StreetResponse getStreetById(Integer id) {
        return this.streetRepository.findById(id)
                .map(this::convertToResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Street with " + id + "not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public StreetResponse getStreetByName(String name) {
        Street street = this.streetRepository.findStreetByName(name);
        if (street == null) {
            throw new ResourceNotFoundException("Street with name " + name + " not found");
        } else {
            return this.convertToResponseDTO(street);
        }
    }

    @Override
    @Transactional
    public StreetResponse saveStreet(StreetRequest streetRequest) {
        this.validateStreetName(streetRequest.name());
        Street street = convertToEntity(streetRequest);
        return convertToResponseDTO(this.streetRepository.save(street));
    }

    @Override
    @Transactional
    public StreetResponse updateStreet(Integer id, StreetRequest streetRequest) {
        Street streetExisting = this.streetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Street with " + id + "not found"));
        streetExisting.setName(streetRequest.name());
        streetExisting.setDistrictId(this.districtRepository.findById(streetRequest.districtId())
                .orElseThrow(() -> new ResourceNotFoundException("District with id " + streetRequest.districtId() + " not found"))
        );
        return convertToResponseDTO(this.streetRepository.save(streetExisting));
    }

    @Override
    @Transactional
    public void deleteStreet(Integer id) {
        try {
            streetRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Street with ID " + id + " not found");
        }
    }

    @Override
    @Transactional
    public boolean existsStreetByName(String name) {
        return this.streetRepository.existsByName(name);
    }

    private Street convertToEntity(StreetRequest dto) {
        Street street = new Street();
        street.setName(dto.name());
        street.setDistrictId(this.districtRepository.findById(dto.districtId())
                .orElseThrow(() -> new ResourceNotFoundException("District with id " + dto.districtId() + " not found")));
        return street;
    }

    private StreetResponse convertToResponseDTO(Street street) {
        return new StreetResponse(street.getId(), street.getName(), street.getDistrictId().getId());
    }
}
