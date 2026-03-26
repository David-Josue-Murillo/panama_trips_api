package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.CampaignTour;
import com.app.panama_trips.persistence.entity.CampaignTourId;
import com.app.panama_trips.persistence.entity.MarketingCampaign;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.repository.CampaignTourRepository;
import com.app.panama_trips.persistence.repository.MarketingCampaignRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.presentation.dto.CampaignTourRequest;
import com.app.panama_trips.presentation.dto.CampaignTourResponse;
import com.app.panama_trips.service.interfaces.ICampaignTourService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CampaignTourService implements ICampaignTourService {

  private final CampaignTourRepository repository;
  private final MarketingCampaignRepository campaignRepository;
  private final TourPlanRepository tourPlanRepository;

  @Override
  public List<CampaignTourResponse> getToursByCampaignId(Integer campaignId) {
    return repository.findByCampaignIdOrderByFeaturedOrder(campaignId).stream()
        .map(CampaignTourResponse::new)
        .toList();
  }

  @Override
  public List<CampaignTourResponse> getCampaignsByTourPlanId(Integer tourPlanId) {
    TourPlan tourPlan = tourPlanRepository.findById(tourPlanId)
        .orElseThrow(() -> new ResourceNotFoundException("Tour Plan not found with id: " + tourPlanId));
    return repository.findByTourPlan(tourPlan).stream()
        .map(CampaignTourResponse::new)
        .toList();
  }

  @Override
  @Transactional
  public CampaignTourResponse addTourToCampaign(CampaignTourRequest request) {
    MarketingCampaign campaign = campaignRepository.findById(request.campaignId())
        .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with id: " + request.campaignId()));
    TourPlan tourPlan = tourPlanRepository.findById(request.tourPlanId())
        .orElseThrow(() -> new ResourceNotFoundException("Tour Plan not found with id: " + request.tourPlanId()));

    CampaignTourId id = CampaignTourId.builder()
        .campaignId(request.campaignId())
        .tourPlanId(request.tourPlanId())
        .build();

    if (repository.existsById(id)) {
      throw new IllegalArgumentException("Tour is already associated with this campaign");
    }

    CampaignTour campaignTour = CampaignTour.builder()
        .id(id)
        .campaign(campaign)
        .tourPlan(tourPlan)
        .featuredOrder(request.featuredOrder() != null ? request.featuredOrder() : 0)
        .specialPrice(request.specialPrice())
        .build();

    return new CampaignTourResponse(repository.save(campaignTour));
  }

  @Override
  @Transactional
  public CampaignTourResponse updateCampaignTour(Integer campaignId, Integer tourPlanId, CampaignTourRequest request) {
    CampaignTourId id = new CampaignTourId(campaignId, tourPlanId);
    CampaignTour campaignTour = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Association not found for campaign " + campaignId + " and tour " + tourPlanId));

    if (request.featuredOrder() != null) {
      campaignTour.setFeaturedOrder(request.featuredOrder());
    }
    if (request.specialPrice() != null) {
      campaignTour.setSpecialPrice(request.specialPrice());
    }

    return new CampaignTourResponse(repository.save(campaignTour));
  }

  @Override
  @Transactional
  public void removeTourFromCampaign(Integer campaignId, Integer tourPlanId) {
    CampaignTourId id = new CampaignTourId(campaignId, tourPlanId);
    if (!repository.existsById(id)) {
      throw new ResourceNotFoundException(
          "Association not found for campaign " + campaignId + " and tour " + tourPlanId);
    }
    repository.deleteById(id);
  }

  @Override
  @Transactional
  public void updateFeaturedOrder(Integer campaignId, Integer tourPlanId, Integer featuredOrder) {
    CampaignTourId id = new CampaignTourId(campaignId, tourPlanId);
    CampaignTour campaignTour = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Association not found for campaign " + campaignId + " and tour " + tourPlanId));
    campaignTour.setFeaturedOrder(featuredOrder);
    repository.save(campaignTour);
  }

  @Override
  @Transactional
  public void updateSpecialPrice(Integer campaignId, Integer tourPlanId, BigDecimal specialPrice) {
    CampaignTourId id = new CampaignTourId(campaignId, tourPlanId);
    CampaignTour campaignTour = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Association not found for campaign " + campaignId + " and tour " + tourPlanId));
    campaignTour.setSpecialPrice(specialPrice);
    repository.save(campaignTour);
  }

  @Override
  public long countToursInCampaign(Integer campaignId) {
    return repository.countToursByCampaign(campaignId);
  }
}
