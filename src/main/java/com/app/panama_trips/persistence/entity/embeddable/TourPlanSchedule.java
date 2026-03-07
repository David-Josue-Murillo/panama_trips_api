package com.app.panama_trips.persistence.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TourPlanSchedule {
  @Column(name = "start_time")
  private LocalTime startTime;

  @Column(name = "end_time")
  private LocalTime endTime;

  @Column(name = "available_days", columnDefinition = "JSONB DEFAULT '[\"MON\", \"TUE\", \"WED\", \"THU\", \"FRI\", \"SAT\", \"SUN\"]'")
  private String availableDays;

  @Column(name = "is_seasonal")
  @Builder.Default
  private Boolean isSeasonal = false;

  @Column(name = "season_start_date")
  private LocalDate seasonStartDate;

  @Column(name = "season_end_date")
  private LocalDate seasonEndDate;
}
