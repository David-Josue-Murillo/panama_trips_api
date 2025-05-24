package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.TourPlanImageRequest;
import com.app.panama_trips.presentation.dto.TourPlanImageResponse;
import com.app.panama_trips.service.implementation.TourPlanImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.app.panama_trips.DataProvider.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TourPlanImageController.class)
public class TourPlanImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TourPlanImageService tourPlanImageService;

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllTourPlan_success() throws Exception {
        Page<TourPlanImageResponse> page = new PageImpl<>(tourPlanImageResponseListMocks);
        when(tourPlanImageService.getAllTourPlanImages(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/tour-plan-image")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(tourPlanImageResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].imageUrl").value(tourPlanImageResponseListMocks.getFirst().imageUrl()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findTourPlanImageById_success() throws Exception {
        TourPlanImageResponse response = tourPlanImageResponseMock;
        when(tourPlanImageService.getTourPlanImageById(any(Integer.class))).thenReturn(response);

        mockMvc.perform(get("/api/tour-plan-image/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.imageUrl").value(response.imageUrl()))
                .andExpect(jsonPath("$.isMain").value(response.isMain()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createTourPlanImage_success() throws Exception {
        TourPlanImageRequest request = tourPlanImageRequestMock;
        TourPlanImageResponse response = tourPlanImageResponseMock;

        when(tourPlanImageService.saveTourPlanImage(any(TourPlanImageRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/tour-plan-image")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.imageUrl").value(response.imageUrl()))
                .andExpect(jsonPath("$.isMain").value(response.isMain()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateTourPlanImage_success() throws Exception {
        TourPlanImageRequest request = tourPlanImageRequestMock;
        TourPlanImageResponse response = new TourPlanImageResponse(1, 1, "https://example.com/updated.jpg", "Updated alt", false, 2);

        when(tourPlanImageService.updateTourPlanImage(any(Integer.class), any(TourPlanImageRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/tour-plan-image/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.imageUrl").value(response.imageUrl()))
                .andExpect(jsonPath("$.isMain").value(response.isMain()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteTourPlanImage_success() throws Exception {
        mockMvc.perform(delete("/api/tour-plan-image/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findImageByTourPlanId_success() throws Exception {
        when(tourPlanImageService.getTourPlanImageById(any(Integer.class))).thenReturn(tourPlanImageResponseMock);

        mockMvc.perform(get("/api/tour-plan-image/tour-plan/1")
                        .param("tourPlanId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tourPlanImageResponseMock.id()))
                .andExpect(jsonPath("$.imageUrl").value(tourPlanImageResponseMock.imageUrl()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findImagesByTourPlanId_success() throws Exception {
        when(tourPlanImageService.getTourPlanImagesByTourPlanId(any(Integer.class))).thenReturn(tourPlanImageResponseListMocks);

        mockMvc.perform(get("/api/tour-plan-image/tour-plan/all/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(tourPlanImageResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$[0].imageUrl").value(tourPlanImageResponseListMocks.getFirst().imageUrl()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findImagesByTourPlanIdOrdered_success() throws Exception {
        when(tourPlanImageService.getTourPlanImagesByTourPlanIdOrderByDisplayOrder(any(Integer.class))).thenReturn(tourPlanImageResponseListMocks);

        mockMvc.perform(get("/api/tour-plan-image/tour-plan/1/ordered"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(tourPlanImageResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$[0].displayOrder").value(tourPlanImageResponseListMocks.getFirst().displayOrder()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findMainImageByTourPlanId_success() throws Exception {
        TourPlanImageResponse response = tourPlanImageResponseListMocks.getFirst();
        when(tourPlanImageService.getMainImageByTourPlanId(any(Integer.class))).thenReturn(response);

        mockMvc.perform(get("/api/tour-plan-image/tour-plan/1/main"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.isMain").value(response.isMain()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findNonMainImagesByTourPlanId_success() throws Exception {
        when(tourPlanImageService.getNonMainImagesByTourPlanIdOrdered(any(Integer.class))).thenReturn(tourPlanImageResponseListMocks);

        mockMvc.perform(get("/api/tour-plan-image/tour-plan/1/non-main"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(tourPlanImageResponseListMocks.getFirst().id()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void countImagesByTourPlanId_success() throws Exception {
        Long count = 5L;
        when(tourPlanImageService.countImagesByTourPlanId(any(Integer.class))).thenReturn(count);

        mockMvc.perform(get("/api/tour-plan-image/tour-plan/1/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(count));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteAllImagesByTourPlanId_success() throws Exception {
        mockMvc.perform(delete("/api/tour-plan-image/tour-plan/1/all")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getMaxDisplayOrderForTourPlan_success() throws Exception {
        Integer maxOrder = 5;
        when(tourPlanImageService.getMaxDisplayOrderForTourPlan(any(Integer.class))).thenReturn(maxOrder);

        mockMvc.perform(get("/api/tour-plan-image/tour-plan/1/max-order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(maxOrder));
    }
}
