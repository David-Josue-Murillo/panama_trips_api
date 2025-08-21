package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.TourFaqRequest;
import com.app.panama_trips.presentation.dto.TourFaqResponse;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITourFaqService {
    // CRUD operations
    Page<TourFaqResponse> getAllFaqs(Pageable pageable);
    TourFaqResponse getFaqById(Integer id);
    TourFaqResponse saveFaq(TourFaqRequest request);
    TourFaqResponse updateFaq(Integer id, TourFaqRequest request);
    void deleteFaq(Integer id);

    // Find operations
    List<TourFaqResponse> findByTourPlanId(Integer tourPlanId);
    List<TourFaqResponse> findByTourPlanIdOrderByDisplayOrderAsc(Integer tourPlanId);
    List<TourFaqResponse> searchByQuestionOrAnswer(String keyword);
    Optional<TourFaqResponse> findByTourPlanIdAndQuestion(Integer tourPlanId, String question);
}
