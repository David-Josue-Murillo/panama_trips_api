package com.app.panama_trips.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.TourFaq;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.repository.TourFaqRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.presentation.dto.TourFaqRequest;
import com.app.panama_trips.presentation.dto.TourFaqResponse;
import com.app.panama_trips.service.interfaces.ITourFaqService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TourFaqService implements ITourFaqService{

    private final TourFaqRepository repository;
    private final TourPlanRepository tourPlanRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<TourFaqResponse> getAllFaqs(Pageable pageable) {
        return this.repository.findAll(pageable)
                .map(TourFaqResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public TourFaqResponse getFaqById(Integer id) {
        TourFaq faq = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TourFaq not found with id: " + id));
        return new TourFaqResponse(faq);
    }

    @Override
    @Transactional
    public TourFaqResponse saveFaq(TourFaqRequest request) {
        validateRequest(request);
        TourFaq newFaq = buildFromRequest(request);
        return new TourFaqResponse(this.repository.save(newFaq));
    }

    @Override
    @Transactional
    public TourFaqResponse updateFaq(Integer id, TourFaqRequest request) {
        TourFaq existingFaq = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TourFaq not found with id: " + id));
        updateFromRequest(existingFaq, request);
        return new TourFaqResponse(this.repository.save(existingFaq));
    }

    @Override
    @Transactional
    public void deleteFaq(Integer id) {
        if (!this.repository.existsById(id)) {
            throw new ResourceNotFoundException("TourFaq not found with id: " + id);
        }
        this.repository.deleteById(id);
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

    // Private methods
    private void validateRequest(TourFaqRequest request) {
        TourPlan tourPlan = findTourPlanOrFail(request.tourPlanId());

        // Validate that the tour plan is active
        if (!"ACTIVE".equals(tourPlan.getStatus())) {
            throw new IllegalStateException("Tour plan is not active");
        }

        // Validate display order uniqueness within the tour plan
        if (!isDisplayOrderUniqueWithinTourPlan(request.tourPlanId(), request.displayOrder())) {
            throw new IllegalStateException("Display order " + request.displayOrder()
                    + " is not unique within tour plan " + request.tourPlanId());
        }

        // Validate question uniqueness within the tour plan
        if (existsByTourPlanIdAndQuestion(request.tourPlanId(), request.question())) {
            throw new IllegalStateException("Question already exists for tour plan " + request.tourPlanId());
        }
    }

    private TourFaq buildFromRequest(TourFaqRequest request) {
        return TourFaq.builder()
                .tourPlan(findTourPlanOrFail(request.tourPlanId()))
                .question(request.question())
                .answer(request.answer())
                .displayOrder(request.displayOrder())
                .build();
    }

    private void updateFromRequest(TourFaq faq, TourFaqRequest request) {
        // Check if we're changing the tour plan
        if (!faq.getTourPlan().getId().equals(request.tourPlanId())) {
            faq.setTourPlan(findTourPlanOrFail(request.tourPlanId()));
        }

        faq.setQuestion(request.question());
        faq.setAnswer(request.answer());
        faq.setDisplayOrder(request.displayOrder());
    }


    private TourPlan findTourPlanOrFail(Integer tourPlanId) {
        return tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("TourPlan not found with id: " + tourPlanId));
    }
}
