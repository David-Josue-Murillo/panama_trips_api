package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.CampaignTourRequest;
import com.app.panama_trips.presentation.dto.CampaignTourResponse;
import com.app.panama_trips.service.interfaces.ICampaignTourService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.app.panama_trips.DataProvider.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test suite for CampaignTourController.
 * Validates endpoint accessibility, security roles, and expected responses.
 */
@WebMvcTest(CampaignTourController.class)
class CampaignTourControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private ICampaignTourService service;

  private CampaignTourResponse response;
  private List<CampaignTourResponse> responseList;
  private CampaignTourRequest request;

  @BeforeEach
  void setUp() {
    response = campaignTourResponseMock();
    responseList = campaignTourResponseListMock();
    request = campaignTourRequestMock();
  }

  @Nested
  @DisplayName("GET /api/campaign-tours - Retrieval")
  class RetrievalTests {

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("CP-001: should get tours by campaign ID")
    void shouldGetToursByCampaignId() throws Exception {
      when(service.getToursByCampaignId(anyInt())).thenReturn(responseList);

      mockMvc.perform(get("/api/campaign-tours/campaign/{id}", 1))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$").isArray())
          .andExpect(jsonPath("$[0].campaignId").value(1));

      verify(service).getToursByCampaignId(1);
    }

    @Test
    @WithMockUser(roles = "CONTENT_MANAGER")
    @DisplayName("CP-002: should get campaigns by tour plan ID")
    void shouldGetCampaignsByTourPlanId() throws Exception {
      when(service.getCampaignsByTourPlanId(anyInt())).thenReturn(responseList);

      mockMvc.perform(get("/api/campaign-tours/tour-plan/{id}", 1))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$").isArray())
          .andExpect(jsonPath("$[0].tourPlanId").value(1));

      verify(service).getCampaignsByTourPlanId(1);
    }
  }

  @Nested
  @DisplayName("POST /api/campaign-tours - Association")
  class AssociationTests {

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("CP-003: should add tour to campaign (ADMIN)")
    void shouldAddTourToCampaignAdmin() throws Exception {
      when(service.addTourToCampaign(any(CampaignTourRequest.class))).thenReturn(response);

      mockMvc.perform(post("/api/campaign-tours")
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.campaignId").value(1));

      verify(service).addTourToCampaign(any(CampaignTourRequest.class));
    }

    @Test
    @WithMockUser(roles = "CONTENT_MANAGER")
    @DisplayName("CP-004: should add tour to campaign (CONTENT_MANAGER)")
    void shouldAddTourToCampaignContentManager() throws Exception {
      when(service.addTourToCampaign(any(CampaignTourRequest.class))).thenReturn(response);

      mockMvc.perform(post("/api/campaign-tours")
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isCreated());
    }

  }

  @Nested
  @DisplayName("PUT /api/campaign-tours - Updates")
  class UpdateTests {

    @Test
    @WithMockUser(roles = "CONTENT_MANAGER")
    @DisplayName("CP-007: should update campaign tour")
    void shouldUpdateCampaignTour() throws Exception {
      when(service.updateCampaignTour(anyInt(), anyInt(), any(CampaignTourRequest.class))).thenReturn(response);

      mockMvc.perform(put("/api/campaign-tours/campaign/{campaignId}/tour-plan/{tourPlanId}", 1, 1)
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk());

      verify(service).updateCampaignTour(eq(1), eq(1), any(CampaignTourRequest.class));
    }
  }

  @Nested
  @DisplayName("DELETE /api/campaign-tours - Removal")
  class RemovalTests {

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("CP-008: should remove tour from campaign")
    void shouldRemoveTourFromCampaign() throws Exception {
      mockMvc.perform(delete("/api/campaign-tours/campaign/{campaignId}/tour-plan/{tourPlanId}", 1, 1)
          .with(csrf()))
          .andExpect(status().isNoContent());

      verify(service).removeTourFromCampaign(1, 1);
    }

  }

  @Nested
  @DisplayName("PATCH /api/campaign-tours - Partial updates")
  class PatchTests {

    @Test
    @WithMockUser(roles = "CONTENT_MANAGER")
    @DisplayName("CP-010: should update featured order")
    void shouldUpdateFeaturedOrder() throws Exception {
      mockMvc.perform(patch("/api/campaign-tours/campaign/{campaignId}/tour-plan/{tourPlanId}/featured-order", 1, 1)
          .with(csrf())
          .param("featuredOrder", "5"))
          .andExpect(status().isOk());

      verify(service).updateFeaturedOrder(1, 1, 5);
    }

    @Test
    @WithMockUser(roles = "CONTENT_MANAGER")
    @DisplayName("CP-011: should update special price")
    void shouldUpdateSpecialPrice() throws Exception {
      mockMvc.perform(patch("/api/campaign-tours/campaign/{campaignId}/tour-plan/{tourPlanId}/special-price", 1, 1)
          .with(csrf())
          .param("specialPrice", "99.99"))
          .andExpect(status().isOk());

      verify(service).updateSpecialPrice(eq(1), eq(1), any(java.math.BigDecimal.class));
    }
  }

  @Nested
  @DisplayName("GET /api/campaign-tours/count - Statistics")
  class StatsTests {

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("CP-012: should count tours in campaign")
    void shouldCountToursInCampaign() throws Exception {
      when(service.countToursInCampaign(1)).thenReturn(10L);

      mockMvc.perform(get("/api/campaign-tours/campaign/{id}/count", 1))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$").value(10));

      verify(service).countToursInCampaign(1);
    }
  }
}
