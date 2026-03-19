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

  
}
