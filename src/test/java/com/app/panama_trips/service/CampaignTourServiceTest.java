package com.app.panama_trips.service;

import com.app.panama_trips.DataProvider;
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
import com.app.panama_trips.service.implementation.CampaignTourService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampaignTourServiceTest {

  @Mock
  private CampaignTourRepository campaignTourRepository;

  @Mock
  private MarketingCampaignRepository campaignRepository;

  @Mock
  private TourPlanRepository tourPlanRepository;

  @InjectMocks
  private CampaignTourService service;

  private CampaignTour campaignTour;
  private MarketingCampaign campaign;
  private TourPlan tourPlan;
  private CampaignTourRequest request;

  @BeforeEach
  void setUp() {
    campaignTour = DataProvider.campaignTourOneMock();
    campaign = DataProvider.marketingCampaignOneMock();
    tourPlan = DataProvider.tourPlanOneMock;
    request = DataProvider.campaignTourRequestMock();
  }

  @Test
  @DisplayName("CP-001: getToursByCampaignId retorna lista de tours")
  void testGetToursByCampaignId() {
    when(campaignTourRepository.findByCampaignIdOrderByFeaturedOrder(1)).thenReturn(List.of(campaignTour));

    List<CampaignTourResponse> result = service.getToursByCampaignId(1);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).campaignId()).isEqualTo(1);
    verify(campaignTourRepository).findByCampaignIdOrderByFeaturedOrder(1);
  }

  @Test
  @DisplayName("CP-001b: getCampaignsByTourPlanId retorna lista de campañas")
  void testGetCampaignsByTourPlanId() {
    when(tourPlanRepository.findById(1)).thenReturn(Optional.of(tourPlan));
    when(campaignTourRepository.findByTourPlan(tourPlan)).thenReturn(List.of(campaignTour));

    List<CampaignTourResponse> result = service.getCampaignsByTourPlanId(1);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).tourPlanId()).isEqualTo(1);
    verify(tourPlanRepository).findById(1);
    verify(campaignTourRepository).findByTourPlan(tourPlan);
  }

  @Test
  @DisplayName("CP-002: addTourToCampaign con éxito")
  void testAddTourToCampaign_Success() {
    when(campaignRepository.findById(1)).thenReturn(Optional.of(campaign));
    when(tourPlanRepository.findById(1)).thenReturn(Optional.of(tourPlan));
    when(campaignTourRepository.existsById(any(CampaignTourId.class))).thenReturn(false);
    when(campaignTourRepository.save(any(CampaignTour.class))).thenReturn(campaignTour);

    CampaignTourResponse result = service.addTourToCampaign(request);

    assertThat(result).isNotNull();
    assertThat(result.campaignId()).isEqualTo(1);
    verify(campaignTourRepository).save(any(CampaignTour.class));
  }

  @Test
  @DisplayName("CP-003: addTourToCampaign falla si ya existe")
  void testAddTourToCampaign_AlreadyExists() {
    when(campaignRepository.findById(1)).thenReturn(Optional.of(campaign));
    when(tourPlanRepository.findById(1)).thenReturn(Optional.of(tourPlan));
    when(campaignTourRepository.existsById(any(CampaignTourId.class))).thenReturn(true);

    assertThatThrownBy(() -> service.addTourToCampaign(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Tour is already associated with this campaign");
  }

  @Test
  @DisplayName("CP-004: updateCampaignTour con éxito")
  void testUpdateCampaignTour_Success() {
    CampaignTourId id = new CampaignTourId(1, 1);
    when(campaignTourRepository.findById(any(CampaignTourId.class))).thenReturn(Optional.of(campaignTour));
    when(campaignTourRepository.save(any(CampaignTour.class))).thenReturn(campaignTour);

    CampaignTourResponse result = service.updateCampaignTour(1, 1, request);

    assertThat(result).isNotNull();
    verify(campaignTourRepository).save(any(CampaignTour.class));
  }

  @Test
  @DisplayName("CP-005: removeTourFromCampaign con éxito")
  void testRemoveTourFromCampaign_Success() {
    CampaignTourId id = new CampaignTourId(1, 1);
    when(campaignTourRepository.existsById(any(CampaignTourId.class))).thenReturn(true);

    service.removeTourFromCampaign(1, 1);

    verify(campaignTourRepository).deleteById(any(CampaignTourId.class));
  }

  @Test
  @DisplayName("CP-006: updateSpecialPrice con éxito")
  void testUpdateSpecialPrice() {
    when(campaignTourRepository.findById(any(CampaignTourId.class))).thenReturn(Optional.of(campaignTour));

    service.updateSpecialPrice(1, 1, BigDecimal.valueOf(50.00));

    verify(campaignTourRepository).save(campaignTour);
    assertThat(campaignTour.getSpecialPrice()).isEqualByComparingTo(BigDecimal.valueOf(50.00));
  }

  @Test
  @DisplayName("CP-007: countToursInCampaign retorna cantidad")
  void testCountToursInCampaign() {
    when(campaignTourRepository.countToursByCampaign(1)).thenReturn(5L);

    long result = service.countToursInCampaign(1);

    assertThat(result).isEqualTo(5L);
    verify(campaignTourRepository).countToursByCampaign(1);
  }

  
}
