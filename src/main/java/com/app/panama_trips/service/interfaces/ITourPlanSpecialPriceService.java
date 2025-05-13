package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.TourPlanSpecialPrice;
import org.springframework.data.domain.Page;
import java.util.Optional;

public interface ITourPlanSpecialPriceService {
    // CRUD operations
    Page<TourPlanSpecialPrice> getAll();
    Optional<TourPlanSpecialPrice> findById(Integer id);
    TourPlanSpecialPrice save(TourPlanSpecialPrice tourPlanSpecialPrice);
    TourPlanSpecialPrice update(Integer id, TourPlanSpecialPrice tourPlanSpecialPrice);
    void deleteById(Integer id);
}