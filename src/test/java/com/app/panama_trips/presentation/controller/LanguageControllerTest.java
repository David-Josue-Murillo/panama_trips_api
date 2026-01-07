package com.app.panama_trips.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LanguageController.class)
public class LanguageControllerTest {

    @Autowired
    private MockMvc mockMvc;

}
