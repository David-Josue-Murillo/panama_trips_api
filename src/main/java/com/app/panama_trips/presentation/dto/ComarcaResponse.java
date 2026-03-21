package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.Comarca;

public record ComarcaResponse(Integer id, String name) {
  public ComarcaResponse(Comarca comarca) {
    this(comarca.getId(), comarca.getName());
  }
}
