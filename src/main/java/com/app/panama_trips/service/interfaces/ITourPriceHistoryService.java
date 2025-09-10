package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.TourPriceHistoryRequest;
import com.app.panama_trips.presentation.dto.TourPriceHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITourPriceHistoryService {

    // CRUD operations
    Page<TourPriceHistoryResponse> getAllTourPriceHistories(Pageable pageable);
    TourPriceHistoryResponse getTourPriceHistoryById(Integer id);
    TourPriceHistoryResponse saveTourPriceHistory(TourPriceHistoryRequest request);
    TourPriceHistoryResponse updateTourPriceHistory(Integer id, TourPriceHistoryRequest request);
    void deleteTourPriceHistory(Integer id);
}
