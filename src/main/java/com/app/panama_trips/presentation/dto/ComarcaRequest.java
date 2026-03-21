package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ComarcaRequest(
    @NotBlank(message = "Comarca name is required") 
    @Size(min = 3, max = 100, message = "Comarca name must be between 3 and 100 characters") 
    String name
  ) {
}
