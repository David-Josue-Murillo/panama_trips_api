package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.StreetRequest;
import com.app.panama_trips.presentation.dto.StreetResponse;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;


public interface IStreetService {
    Page<StreetResponse> getAllStreet(Integer page, Integer size, Boolean enabledPagination);
    List<StreetResponse> getAllStreetByDistrictId(Integer districtId);
    StreetResponse getStreetById(Integer id);
    StreetResponse getStreetByName(String name);
    StreetResponse saveStreet(StreetRequest StreetDTO);
    StreetResponse updateStreet(Integer id, StreetRequest StreetDTO);
    void deleteStreet(Integer id);
    boolean existsStreetByName(String name);
}
