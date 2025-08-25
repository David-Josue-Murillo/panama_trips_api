package com.app.panama_trips.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.panama_trips.presentation.dto.TourFaqRequest;
import com.app.panama_trips.presentation.dto.TourFaqResponse;
import com.app.panama_trips.service.interfaces.ITourFaqService;

@Service
public class TourFaqService implements ITourFaqService{

    @Override
    public Page<TourFaqResponse> getAllFaqs(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllFaqs'");
    }

    @Override
    public TourFaqResponse getFaqById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFaqById'");
    }

    @Override
    public TourFaqResponse saveFaq(TourFaqRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveFaq'");
    }

    @Override
    public TourFaqResponse updateFaq(Integer id, TourFaqRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateFaq'");
    }

    @Override
    public void deleteFaq(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteFaq'");
    }

    @Override
    public List<TourFaqResponse> findByTourPlanId(Integer tourPlanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByTourPlanId'");
    }

    @Override
    public List<TourFaqResponse> findByTourPlanIdOrderByDisplayOrderAsc(Integer tourPlanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByTourPlanIdOrderByDisplayOrderAsc'");
    }

    @Override
    public List<TourFaqResponse> searchByQuestionOrAnswer(String keyword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchByQuestionOrAnswer'");
    }

    @Override
    public Optional<TourFaqResponse> findByTourPlanIdAndQuestion(Integer tourPlanId, String question) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByTourPlanIdAndQuestion'");
    }

    @Override
    public List<TourFaqResponse> getTopFaqsByTourPlan(Integer tourPlanId, int limit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTopFaqsByTourPlan'");
    }

    @Override
    public void reorderFaqs(Integer tourPlanId, List<Integer> faqIdsInOrder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reorderFaqs'");
    }

    @Override
    public void bulkCreateFaqs(List<TourFaqRequest> requests) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bulkCreateFaqs'");
    }

    @Override
    public void bulkUpdateFaqs(List<TourFaqRequest> requests) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bulkUpdateFaqs'");
    }

    @Override
    public void bulkDeleteFaqs(List<Integer> faqIds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bulkDeleteFaqs'");
    }

    @Override
    public boolean existsByTourPlanIdAndQuestion(Integer tourPlanId, String question) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsByTourPlanIdAndQuestion'");
    }

    @Override
    public boolean isDisplayOrderUniqueWithinTourPlan(Integer tourPlanId, Integer displayOrder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isDisplayOrderUniqueWithinTourPlan'");
    }

    @Override
    public long countByTourPlanId(Integer tourPlanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'countByTourPlanId'");
    }

}
