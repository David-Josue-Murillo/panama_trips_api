package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.CampaignTourRequest;
import com.app.panama_trips.presentation.dto.CampaignTourResponse;
import com.app.panama_trips.service.interfaces.ICampaignTourService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/campaign-tours")
@RequiredArgsConstructor
public class CampaignTourController {

  private final ICampaignTourService service;

  @GetMapping("/campaign/{campaignId}")
  public ResponseEntity<List<CampaignTourResponse>> getByCampaignId(@PathVariable Integer campaignId) {
    return ResponseEntity.ok(service.getToursByCampaignId(campaignId));
  }

  @GetMapping("/tour-plan/{tourPlanId}")
  public ResponseEntity<List<CampaignTourResponse>> getByTourPlanId(@PathVariable Integer tourPlanId) {
    return ResponseEntity.ok(service.getCampaignsByTourPlanId(tourPlanId));
  }

  @PostMapping
  public ResponseEntity<CampaignTourResponse> addTourToCampaign(@Valid @RequestBody CampaignTourRequest request) {
    return ResponseEntity.status(201).body(service.addTourToCampaign(request));
  }

  @PutMapping("/campaign/{campaignId}/tour-plan/{tourPlanId}")
  public ResponseEntity<CampaignTourResponse> updateAssociation(
      @PathVariable Integer campaignId,
      @PathVariable Integer tourPlanId,
      @Valid @RequestBody CampaignTourRequest request) {
    return ResponseEntity.ok(service.updateCampaignTour(campaignId, tourPlanId, request));
  }

  @DeleteMapping("/campaign/{campaignId}/tour-plan/{tourPlanId}")
  public ResponseEntity<Void> removeTourFromCampaign(
      @PathVariable Integer campaignId,
      @PathVariable Integer tourPlanId) {
    service.removeTourFromCampaign(campaignId, tourPlanId);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/campaign/{campaignId}/tour-plan/{tourPlanId}/featured-order")
  public ResponseEntity<Void> updateFeaturedOrder(
      @PathVariable Integer campaignId,
      @PathVariable Integer tourPlanId,
      @RequestParam Integer featuredOrder) {
    service.updateFeaturedOrder(campaignId, tourPlanId, featuredOrder);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/campaign/{campaignId}/tour-plan/{tourPlanId}/special-price")
  public ResponseEntity<Void> updateSpecialPrice(
      @PathVariable Integer campaignId,
      @PathVariable Integer tourPlanId,
      @RequestParam BigDecimal specialPrice) {
    service.updateSpecialPrice(campaignId, tourPlanId, specialPrice);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/campaign/{campaignId}/count")
  public ResponseEntity<Long> countToursInCampaign(@PathVariable Integer campaignId) {
    return ResponseEntity.ok(service.countToursInCampaign(campaignId));
  }
}
