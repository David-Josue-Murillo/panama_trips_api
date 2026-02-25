package com.app.panama_trips.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.TourFaq;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.enums.TourPlanStatus;
import com.app.panama_trips.persistence.repository.TourFaqRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.presentation.dto.TourFaqRequest;
import com.app.panama_trips.presentation.dto.TourFaqResponse;
import com.app.panama_trips.service.interfaces.ITourFaqService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TourFaqService implements ITourFaqService {

    private final TourFaqRepository repository;
    private final TourPlanRepository tourPlanRepository;

    // CRUD operations
    @Override
    public Page<TourFaqResponse> getAllFaqs(Pageable pageable) {
        return repository.findAll(pageable).map(TourFaqResponse::new);
    }

    @Override
    public TourFaqResponse getFaqById(Integer id) {
        return new TourFaqResponse(findFaqOrThrow(id));
    }

    @Override
    @Transactional
    public TourFaqResponse saveFaq(TourFaqRequest request) {
        validateRequest(request);
        return new TourFaqResponse(repository.save(buildFromRequest(request)));
    }

    @Override
    @Transactional
    public TourFaqResponse updateFaq(Integer id, TourFaqRequest request) {
        TourFaq existingFaq = findFaqOrThrow(id);
        updateFromRequest(existingFaq, request);
        return new TourFaqResponse(repository.save(existingFaq));
    }

    @Override
    @Transactional
    public void deleteFaq(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("TourFaq not found with id: " + id);
        }
        repository.deleteById(id);
    }

    // Find operations
    @Override
    public List<TourFaqResponse> findByTourPlanId(Integer tourPlanId) {
        validateTourPlanExists(tourPlanId);
        return toResponseList(repository.findByTourPlan_Id(tourPlanId));
    }

    @Override
    public List<TourFaqResponse> findByTourPlanIdOrderByDisplayOrderAsc(Integer tourPlanId) {
        return toResponseList(repository.findByTourPlan_IdOrderByDisplayOrderAsc(tourPlanId));
    }

    @Override
    public List<TourFaqResponse> searchByQuestionOrAnswer(String keyword) {
        return toResponseList(repository.searchByKeyword(keyword));
    }

    @Override
    public Optional<TourFaqResponse> findByTourPlanIdAndQuestion(Integer tourPlanId, String question) {
        return repository.findByTourPlan_IdAndQuestionIgnoreCase(tourPlanId, question)
                .map(TourFaqResponse::new);
    }

    // Specialized queries
    @Override
    public List<TourFaqResponse> getTopFaqsByTourPlan(Integer tourPlanId, int limit) {
        validateTourPlanExists(tourPlanId);
        return toResponseList(repository.findByTourPlan_IdOrderByDisplayOrderAsc(tourPlanId, PageRequest.of(0, limit)));
    }

    @Override
    @Transactional
    public void reorderFaqs(Integer tourPlanId, List<Integer> faqIdsInOrder) {
        List<TourFaq> toSave = new ArrayList<>();
        for (int i = 0; i < faqIdsInOrder.size(); i++) {
            Integer faqId = faqIdsInOrder.get(i);
            TourFaq faq = findFaqOrThrow(faqId);
            if (!faq.getTourPlan().getId().equals(tourPlanId)) {
                throw new IllegalArgumentException(
                        "FAQ with id " + faqId + " does not belong to tour plan " + tourPlanId);
            }
            faq.setDisplayOrder(i + 1);
            toSave.add(faq);
        }
        repository.saveAll(toSave);
    }

    // Bulk operations
    @Override
    @Transactional
    public void bulkCreateFaqs(List<TourFaqRequest> requests) {
        requests.forEach(this::validateRequest);
        List<TourFaq> faqs = requests.stream()
                .map(this::buildFromRequest)
                .toList();
        repository.saveAll(faqs);
    }

    @Override
    @Transactional
    public void bulkDeleteFaqs(List<Integer> faqIds) {
        for (Integer faqId : faqIds) {
            if (!repository.existsById(faqId)) {
                throw new ResourceNotFoundException("TourFaq not found with id: " + faqId);
            }
        }
        repository.deleteAllById(faqIds);
    }

    // Check operations
    @Override
    public boolean existsByTourPlanIdAndQuestion(Integer tourPlanId, String question) {
        return repository.existsByTourPlan_IdAndQuestionIgnoreCase(tourPlanId, question);
    }

    @Override
    public boolean isDisplayOrderUniqueWithinTourPlan(Integer tourPlanId, Integer displayOrder) {
        return !repository.existsByTourPlan_IdAndDisplayOrder(tourPlanId, displayOrder);
    }

    @Override
    public long countByTourPlanId(Integer tourPlanId) {
        return repository.countByTourPlan_Id(tourPlanId);
    }

    // Private helper methods
    private TourFaq findFaqOrThrow(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TourFaq not found with id: " + id));
    }

    private TourPlan findTourPlanOrThrow(Integer tourPlanId) {
        return tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("TourPlan not found with id: " + tourPlanId));
    }

    private void validateTourPlanExists(Integer tourPlanId) {
        if (!tourPlanRepository.existsById(tourPlanId)) {
            throw new ResourceNotFoundException("TourPlan not found with id: " + tourPlanId);
        }
    }

    private void validateRequest(TourFaqRequest request) {
        TourPlan tourPlan = findTourPlanOrThrow(request.tourPlanId());
        if (tourPlan.getStatus() != TourPlanStatus.ACTIVE) {
            throw new IllegalStateException("Tour plan is not active");
        }
        if (repository.existsByTourPlan_IdAndDisplayOrder(request.tourPlanId(), request.displayOrder())) {
            throw new IllegalStateException("Display order " + request.displayOrder()
                    + " is not unique within tour plan " + request.tourPlanId());
        }
        if (repository.existsByTourPlan_IdAndQuestionIgnoreCase(request.tourPlanId(), request.question())) {
            throw new IllegalStateException("Question already exists for tour plan " + request.tourPlanId());
        }
    }

    private TourFaq buildFromRequest(TourFaqRequest request) {
        return TourFaq.builder()
                .tourPlan(findTourPlanOrThrow(request.tourPlanId()))
                .question(request.question())
                .answer(request.answer())
                .displayOrder(request.displayOrder())
                .build();
    }

    private void updateFromRequest(TourFaq faq, TourFaqRequest request) {
        if (!faq.getTourPlan().getId().equals(request.tourPlanId())) {
            faq.setTourPlan(findTourPlanOrThrow(request.tourPlanId()));
        }
        faq.setQuestion(request.question());
        faq.setAnswer(request.answer());
        faq.setDisplayOrder(request.displayOrder());
    }

    private List<TourFaqResponse> toResponseList(List<TourFaq> faqs) {
        return faqs.stream()
                .map(TourFaqResponse::new)
                .toList();
    }
}
