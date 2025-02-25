package com.app.panama_trips.presentation.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "password"})
public record AuthLoginRequest(String username, String password) {
}
