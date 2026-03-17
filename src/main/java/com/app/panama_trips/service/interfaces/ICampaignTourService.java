package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.CampaignTourRequest;
import com.app.panama_trips.presentation.dto.CampaignTourResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * Contrato de servicio para la gestión de la relación entre campañas y tours
 * (CampaignTour).
 * Permite asociar tours a campañas, gestionar precios especiales y orden de
 * visualización.
 */
public interface ICampaignTourService {

  /**
   * Obtiene todos los tours asociados a una campaña específica.
   *
   * @param campaignId identificador de la campaña
   * @return lista de tours asociados a la campaña
   */
  List<CampaignTourResponse> getToursByCampaignId(Integer campaignId);

  /**
   * Obtiene todas las campañas asociadas a un plan de tour específico.
   *
   * @param tourPlanId identificador del plan de tour
   * @return lista de campañas asociadas al tour
   */
  List<CampaignTourResponse> getCampaignsByTourPlanId(Integer tourPlanId);

  /**
   * Asocia un tour a una campaña con un precio especial y un orden específico.
   *
   * @param request datos de la asociación
   * @return la asociación creada
   */
  CampaignTourResponse addTourToCampaign(CampaignTourRequest request);

  /**
   * Actualiza los datos de una asociación existente (precio especial u orden).
   *
   * @param campaignId identificador de la campaña
   * @param tourPlanId identificador del tour
   * @param request    nuevos datos
   * @return la asociación actualizada
   */
  CampaignTourResponse updateCampaignTour(Integer campaignId, Integer tourPlanId, CampaignTourRequest request);

  /**
   * Elimina la asociación entre una campaña y un tour.
   *
   * @param campaignId identificador de la campaña
   * @param tourPlanId identificador del tour
   */
  void removeTourFromCampaign(Integer campaignId, Integer tourPlanId);

  /**
   * Actualiza el orden de visualización destacado de un tour en una campaña.
   *
   * @param campaignId    identificador de la campaña
   * @param tourPlanId    identificador del tour
   * @param featuredOrder nuevo orden
   */
  void updateFeaturedOrder(Integer campaignId, Integer tourPlanId, Integer featuredOrder);

  /**
   * Actualiza el precio especial de un tour en una campaña.
   *
   * @param campaignId   identificador de la campaña
   * @param tourPlanId   identificador del tour
   * @param specialPrice nuevo precio especial
   */
  void updateSpecialPrice(Integer campaignId, Integer tourPlanId, BigDecimal specialPrice);

  /**
   * Cuenta cuántos tours están asociados a una campaña.
   *
   * @param campaignId identificador de la campaña
   * @return cantidad de tours
   */
  long countToursInCampaign(Integer campaignId);
}
