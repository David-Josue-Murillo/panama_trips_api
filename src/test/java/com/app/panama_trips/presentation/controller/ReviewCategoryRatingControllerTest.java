package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.ReviewCategoryRatingRequest;
import com.app.panama_trips.presentation.dto.ReviewCategoryRatingResponse;
import com.app.panama_trips.service.implementation.ReviewCategoryRatingService;
import com.app.panama_trips.utility.ParseJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import java.util.List;

import static com.app.panama_trips.DataProvider.reviewCategoryRatingRequestMock;
import static com.app.panama_trips.DataProvider.reviewCategoryRatingResponseListMock;
import static com.app.panama_trips.DataProvider.reviewCategoryRatingResponseMock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ReviewCategoryRatingController.class)
public class ReviewCategoryRatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewCategoryRatingService reviewCategoryService;

    private ReviewCategoryRatingRequest request;
    private ReviewCategoryRatingResponse response;
    private List<ReviewCategoryRatingResponse> responseList;

    @BeforeEach
    void setUp() {
        request = reviewCategoryRatingRequestMock();
        response = reviewCategoryRatingResponseMock();
        responseList = reviewCategoryRatingResponseListMock();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should return all review category ratings with pagination when getAllReviewCategoryRatings is called")
    void getAllReviewCategoryRatings_success() throws Exception {
        // Given
        Page<ReviewCategoryRatingResponse> page = new PageImpl<>(responseList);
        when(reviewCategoryService.getAllReviewCategoryRatings(any(Pageable.class))).thenReturn(page);

        // When/Then
        mockMvc.perform(get("/api/review-category-ratings")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].reviewId").value(response.reviewId()))
                .andExpect(jsonPath("$.content[0].categoryId").value(response.categoryId()))
                .andExpect(jsonPath("$.content[0].rating").value(response.rating()));

        verify(reviewCategoryService).getAllReviewCategoryRatings(any(Pageable.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should return review category rating by composite id when exists")
    void getReviewCategoryRatingById_success() throws Exception {
        // Given
        Integer reviewId = response.reviewId();
        Integer categoryId = response.categoryId();
        when(reviewCategoryService.getReviewCategoryRatingById(reviewId, categoryId)).thenReturn(response);

        // When/Then
        mockMvc.perform(get("/api/review-category-ratings/reviews/{reviewId}/categories/{categoryId}", reviewId,
                categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value(response.reviewId()))
                .andExpect(jsonPath("$.categoryId").value(response.categoryId()))
                .andExpect(jsonPath("$.rating").value(response.rating()));

        verify(reviewCategoryService).getReviewCategoryRatingById(reviewId, categoryId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should save review category rating successfully")
    void saveReviewCategoryRating_success() throws Exception {
        // Given
        when(reviewCategoryService.saveReviewCategoryRating(any(ReviewCategoryRatingRequest.class)))
                .thenReturn(response);

        // When/Then
        mockMvc.perform(post("/api/review-category-ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ParseJson.asJsonString(request))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reviewId").value(response.reviewId()))
                .andExpect(jsonPath("$.categoryId").value(response.categoryId()))
                .andExpect(jsonPath("$.rating").value(response.rating()));

        verify(reviewCategoryService).saveReviewCategoryRating(any(ReviewCategoryRatingRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should update review category rating successfully")
    void updateReviewCategoryRating_success() throws Exception {
        // Given
        Integer reviewId = request.reviewId();
        Integer categoryId = request.categoryId();
        when(reviewCategoryService.updateReviewCategoryRating(eq(reviewId), eq(categoryId),
                any(ReviewCategoryRatingRequest.class))).thenReturn(response);

        // When/Then
        mockMvc.perform(put("/api/review-category-ratings/reviews/{reviewId}/categories/{categoryId}", reviewId,
                categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ParseJson.asJsonString(request))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value(response.reviewId()))
                .andExpect(jsonPath("$.categoryId").value(response.categoryId()))
                .andExpect(jsonPath("$.rating").value(response.rating()));

        verify(reviewCategoryService).updateReviewCategoryRating(eq(reviewId), eq(categoryId),
                any(ReviewCategoryRatingRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should delete review category rating successfully")
    void deleteReviewCategoryRating_success() throws Exception {
        // Given
        Integer reviewId = request.reviewId();
        Integer categoryId = request.categoryId();

        // When/Then
        mockMvc.perform(delete("/api/review-category-ratings/reviews/{reviewId}/categories/{categoryId}", reviewId,
                categoryId)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(reviewCategoryService).deleteReviewCategoryRating(reviewId, categoryId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should return ratings by review successfully")
    void getRatingsByReview_success() throws Exception {
        // Given
        Integer reviewId = request.reviewId();
        when(reviewCategoryService.getRatingsByReview(reviewId)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/review-category-ratings/reviews/{reviewId}", reviewId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reviewId").value(response.reviewId()))
                .andExpect(jsonPath("$[0].categoryId").value(response.categoryId()))
                .andExpect(jsonPath("$[0].rating").value(response.rating()));

        verify(reviewCategoryService).getRatingsByReview(reviewId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should return ratings by category successfully")
    void getRatingsByCategory_success() throws Exception {
        // Given
        Integer categoryId = request.categoryId();
        when(reviewCategoryService.getRatingsByCategory(categoryId)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/review-category-ratings/categories/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reviewId").value(response.reviewId()))
                .andExpect(jsonPath("$[0].categoryId").value(response.categoryId()))
                .andExpect(jsonPath("$[0].rating").value(response.rating()));

        verify(reviewCategoryService).getRatingsByCategory(categoryId);
    }
}