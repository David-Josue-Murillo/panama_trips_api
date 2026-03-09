package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Provider;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.embeddable.*;
import com.app.panama_trips.persistence.repository.ProviderRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.presentation.dto.TourPlanRequest;
import com.app.panama_trips.presentation.dto.TourPlanResponse;
import com.app.panama_trips.service.interfaces.ITourPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TourPlanService implements ITourPlanService {

    private final TourPlanRepository tourPlanRepository;
    private final ProviderRepository providerRepository;

    @Override
    public Page<TourPlanResponse> getAllTourPlan(Pageable pageable) {
        return tourPlanRepository.findAll(pageable)
                .map(TourPlanResponse::new);
    }

    @Override
    public TourPlanResponse getTourPlanById(Integer id) {
        return new TourPlanResponse(findTourPlanOrThrow(id));
    }

    @Override
    @Transactional
    public TourPlanResponse saveTourPlan(TourPlanRequest tourPlanRequest) {
        validateTitleUniqueness(tourPlanRequest.title());
        TourPlan newTourPlan = buildTourPlanFromRequest(tourPlanRequest);
        return new TourPlanResponse(tourPlanRepository.save(newTourPlan));
    }

    @Override
    @Transactional
    public TourPlanResponse updateTourPlan(Integer id, TourPlanRequest tourPlanRequest) {
        TourPlan tourPlan = findTourPlanOrThrow(id);
        updateTourPlanEntity(tourPlan, tourPlanRequest);
        return new TourPlanResponse(tourPlanRepository.save(tourPlan));
    }

    @Override
    @Transactional
    public void deleteTourPlan(Integer id) {
        if (!tourPlanRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tour Plan with id " + id + " not found");
        }
        tourPlanRepository.deleteById(id);
    }

    @Override
    public TourPlanResponse getTourPlanByTitle(String title) {
        return tourPlanRepository.findByTitleIgnoreCase(title)
                .map(TourPlanResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("Tour Plan with title " + title + " not found"));
    }

    @Override
    public List<TourPlanResponse> getTourPlanByPrice(BigDecimal price) {
        return toResponseList(tourPlanRepository.findByPricing_Price(price));
    }

    @Override
    public Page<TourPlanResponse> getTourPlanByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice,
            Pageable pageable) {
        return tourPlanRepository.findByPricing_PriceBetween(minPrice, maxPrice, pageable)
                .map(TourPlanResponse::new);
    }

    @Override
    public List<TourPlanResponse> getTourPlanByDuration(Integer duration) {
        return toResponseList(tourPlanRepository.findByDuration(duration));
    }

    @Override
    public Page<TourPlanResponse> getTourPlanByDurationBetween(Integer minDuration, Integer maxDuration,
            Pageable pageable) {
        return tourPlanRepository.findByDurationBetween(minDuration, maxDuration, pageable)
                .map(TourPlanResponse::new);
    }

    @Override
    public List<TourPlanResponse> getTourPlanByAvailableSpots(Integer availableSpots) {
        return toResponseList(tourPlanRepository.findByAvailableSpots(availableSpots));
    }

    @Override
    public Page<TourPlanResponse> getTourPlanByAvailableSpotsBetween(Integer minSpots, Integer maxSpots,
            Pageable pageable) {
        return tourPlanRepository.findByAvailableSpotsBetween(minSpots, maxSpots, pageable)
                .map(TourPlanResponse::new);
    }

    @Override
    public List<TourPlanResponse> getTourPlanByProviderId(Integer providerId) {
        return toResponseList(tourPlanRepository.findByProvider_Id(providerId));
    }

    @Override
    public List<TourPlanResponse> getTourPlanByTitleAndPrice(String title, BigDecimal price) {
        return toResponseList(tourPlanRepository.findByTitleContainingIgnoreCaseAndPricing_Price(title, price));
    }

    @Override
    public Page<TourPlanResponse> getTourPlanByTitleAndPriceBetween(String title, BigDecimal minPrice,
            BigDecimal maxPrice, Pageable pageable) {
        return tourPlanRepository
                .findByTitleContainingIgnoreCaseAndPricing_PriceBetween(title, minPrice, maxPrice, pageable)
                .map(TourPlanResponse::new);
    }

    @Override
    public Page<TourPlanResponse> getTourPlanByTitleAndPriceBetweenAndDurationBetween(String title, BigDecimal minPrice,
            BigDecimal maxPrice, Integer minDuration, Integer maxDuration, Pageable pageable) {
        return tourPlanRepository
                .findByTitleContainingIgnoreCaseAndPricing_PriceBetweenAndDurationBetween(title, minPrice, maxPrice,
                        minDuration, maxDuration, pageable)
                .map(TourPlanResponse::new);
    }

    @Override
    public List<TourPlanResponse> getTop10TourPlanByTitleContaining(String keyword, Pageable pageable) {
        return toResponseList(
                tourPlanRepository.findTop10ByTitleContainingIgnoreCaseOrderByTitleAsc(keyword, pageable));
    }

    @Override
    public boolean existsTourPlanByTitle(String title) {
        return tourPlanRepository.existsByTitleIgnoreCase(title);
    }

    @Override
    public long countTourPlan() {
        return tourPlanRepository.count();
    }

    // Private helpers

    private TourPlan findTourPlanOrThrow(Integer id) {
        return tourPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tour Plan with id " + id + " not found"));
    }

    private Provider findProviderOrFail(Integer providerId) {
        return providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider with id " + providerId + " not found"));
    }

    private void validateTitleUniqueness(String title) {
        if (tourPlanRepository.existsByTitleIgnoreCase(title)) {
            throw new IllegalArgumentException("Tour Plan with title " + title + " already exists");
        }
    }

    private TourPlan buildTourPlanFromRequest(TourPlanRequest tourPlanRequest) {
        TourPlanPricing pricing = TourPlanPricing.builder()
                .price(tourPlanRequest.price())
                .build();

        TourPlanSchedule schedule = TourPlanSchedule.builder()
                .startTime(tourPlanRequest.startTime())
                .endTime(tourPlanRequest.endTime())
                .availableDays(tourPlanRequest.availableDays())
                .build();

        TourPlanMedia media = TourPlanMedia.builder()
                .imageGallery(tourPlanRequest.imageGallery())
                .build();

        TourPlan.TourPlanBuilder builder = TourPlan.builder()
                .title(tourPlanRequest.title())
                .description(tourPlanRequest.description())
                .pricing(pricing)
                .schedule(schedule)
                .media(media)
                .duration(tourPlanRequest.duration())
                .availableSpots(tourPlanRequest.availableSpots())
                .provider(findProviderOrFail(tourPlanRequest.providerId()))
                .includedServices(tourPlanRequest.includedServices())
                .excludedServices(tourPlanRequest.excludedServices())
                .whatToBring(tourPlanRequest.whatToBring())
                .tags(tourPlanRequest.tags())
                .languageOptions(tourPlanRequest.languageOptions());

        if (tourPlanRequest.status() != null) {
            builder.status(tourPlanRequest.status());
        }
        if (tourPlanRequest.difficultyLevel() != null) {
            builder.difficultyLevel(tourPlanRequest.difficultyLevel());
        }
        return builder.build();
    }

    private void updateTourPlanEntity(TourPlan tourPlan, TourPlanRequest tourPlanRequest) {
        tourPlan.setTitle(tourPlanRequest.title());
        tourPlan.setDescription(tourPlanRequest.description());

        if (tourPlan.getPricing() == null) {
            tourPlan.setPricing(new TourPlanPricing());
        }
        tourPlan.getPricing().setPrice(tourPlanRequest.price());

        if (tourPlan.getSchedule() == null) {
            tourPlan.setSchedule(new TourPlanSchedule());
        }
        tourPlan.getSchedule().setStartTime(tourPlanRequest.startTime());
        tourPlan.getSchedule().setEndTime(tourPlanRequest.endTime());
        if (tourPlanRequest.availableDays() != null) {
            tourPlan.getSchedule().setAvailableDays(tourPlanRequest.availableDays());
        }

        if (tourPlan.getMedia() == null) {
            tourPlan.setMedia(new TourPlanMedia());
        }
        tourPlan.getMedia().setImageGallery(tourPlanRequest.imageGallery());

        tourPlan.setIncludedServices(tourPlanRequest.includedServices());
        tourPlan.setExcludedServices(tourPlanRequest.excludedServices());
        tourPlan.setWhatToBring(tourPlanRequest.whatToBring());
        tourPlan.setTags(tourPlanRequest.tags());
        tourPlan.setLanguageOptions(tourPlanRequest.languageOptions());

        tourPlan.setDuration(tourPlanRequest.duration());
        tourPlan.setAvailableSpots(tourPlanRequest.availableSpots());
        tourPlan.setProvider(findProviderOrFail(tourPlanRequest.providerId()));

        if (tourPlanRequest.status() != null) {
            tourPlan.setStatus(tourPlanRequest.status());
        }
        if (tourPlanRequest.difficultyLevel() != null) {
            tourPlan.setDifficultyLevel(tourPlanRequest.difficultyLevel());
        }
    }

    private List<TourPlanResponse> toResponseList(List<TourPlan> tourPlans) {
        return tourPlans.stream()
                .map(TourPlanResponse::new)
                .toList();
    }
}
