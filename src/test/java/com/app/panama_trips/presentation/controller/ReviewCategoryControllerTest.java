package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.persistence.entity.ReviewCategory;
import com.app.panama_trips.presentation.dto.ReviewCategoryRequest;
import com.app.panama_trips.presentation.dto.ReviewCategoryResponse;
import com.app.panama_trips.service.implementation.ReviewCategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewCategoryController.class)
public class ReviewCategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewCategoryService reviewCategoryService;

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(obj);
    }

    @Test
  @WithMockUser(username = "admin", roles = { "ADMIN" })
  void getAllReviewCategories_success() throws Exception {
    Page<ReviewCategoryResponse> page = new PageImpl<>(reviewCategoryResponseListMock());
    when(reviewCategoryService.getAllReviewCategories(any(Pageable.class))).thenReturn(page);

    mockMvc.perform(get("/api/review-categories")
        .param("page", "0")
        .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].id").value(reviewCategoryResponseListMock().getFirst().id()))
        .andExpect(jsonPath("$.content[0].name").value(reviewCategoryResponseListMock().getFirst().name()))
        .andExpect(
            jsonPath("$.content[0].description").value(reviewCategoryResponseListMock().getFirst().description()));
  }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void getReviewCategoryById_success() throws Exception {
        ReviewCategory category = reviewCategoryOneMock();
        category.setId(1);
        ReviewCategoryResponse reviewCategoryResponse = new ReviewCategoryResponse(category);
        when(reviewCategoryService.getReviewCategoryById(anyInt())).thenReturn(reviewCategoryResponse);

        mockMvc.perform(get("/api/review-categories/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(reviewCategoryResponse.id()))
            .andExpect(jsonPath("$.name").value(reviewCategoryResponse.name()))
            .andExpect(jsonPath("$.description").value(reviewCategoryResponse.description()));
    }

    @Test
  @WithMockUser(username = "admin", roles = { "ADMIN" })
  void saveReviewCategory_success() throws Exception {
    ReviewCategoryResponse reviewCategoryResponse = reviewCategoryResponseMock();
    ReviewCategoryRequest request = reviewCategoryRequestMock();
    when(reviewCategoryService.saveReviewCategory(any(ReviewCategoryRequest.class))).thenReturn(reviewCategoryResponse);

    mockMvc.perform(post("/api/review-categories")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(request))
        .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value(reviewCategoryResponse.name()))
        .andExpect(jsonPath("$.description").value(reviewCategoryResponse.description()));
  }

  @Test
  @WithMockUser(username = "admin", roles = { "ADMIN" })
  void updateReviewCategory_success() throws Exception {
    ReviewCategoryResponse reviewCategoryResponse = reviewCategoryResponseMock();
    ReviewCategoryRequest request = reviewCategoryRequestMock();
    when(reviewCategoryService.updateReviewCategory(anyInt(), any(ReviewCategoryRequest.class)))
        .thenReturn(reviewCategoryResponse);

    mockMvc.perform(put("/api/review-categories/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(request))
        .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(reviewCategoryResponse.name()))
        .andExpect(jsonPath("$.description").value(reviewCategoryResponse.description()));
  }

  @Test
  @WithMockUser(username = "admin", roles = { "ADMIN" })
  void deleteReviewCategory_success() throws Exception {
    doNothing().when(reviewCategoryService).deleteReviewCategory(anyInt());

    mockMvc.perform(delete("/api/review-categories/{id}", 1)
        .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser(username = "admin", roles = { "ADMIN" })
  void getAllReviewCategories_withEmptyPage_shouldReturnEmptyPage() throws Exception {
    Page<ReviewCategoryResponse> emptyPage = new PageImpl<>(java.util.List.of());
    when(reviewCategoryService.getAllReviewCategories(any(Pageable.class))).thenReturn(emptyPage);

    mockMvc.perform(get("/api/review-categories")
        .param("page", "0")
        .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isEmpty())
        .andExpect(jsonPath("$.totalElements").value(0));
  }

  @Test
  @WithMockUser(username = "admin", roles = { "ADMIN" })
  void saveReviewCategory_withNullDescription_shouldSuccess() throws Exception {
    ReviewCategoryRequest request = new ReviewCategoryRequest("Test Category", null);
    ReviewCategoryResponse response = new ReviewCategoryResponse(
        ReviewCategory.builder()
            .id(1)
            .name("Test Category")
            .description(null)
            .build());
    when(reviewCategoryService.saveReviewCategory(any(ReviewCategoryRequest.class))).thenReturn(response);

    mockMvc.perform(post("/api/review-categories")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(request))
        .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Test Category"))
        .andExpect(jsonPath("$.description").isEmpty());
  }

}