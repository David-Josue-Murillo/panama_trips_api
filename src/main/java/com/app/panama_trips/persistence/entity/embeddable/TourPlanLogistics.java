package com.app.panama_trips.persistence.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TourPlanLogistics {
  @Column(name = "meeting_point")
  private String meetingPoint;

  @Column(name = "meeting_point_coordinates", length = 100)
  private String meetingPointCoordinates;

  @Column(name = "tour_route", columnDefinition = "text")
  private String tourRoute;

  @Column(name = "booking_deadline_hours")
  @Builder.Default
  private Integer bookingDeadlineHours = 24;

  @Column(name = "min_advance_booking_days")
  @Builder.Default
  private Integer minAdvanceBookingDays = 1;

  @Column(name = "max_advance_booking_days")
  @Builder.Default
  private Integer maxAdvanceBookingDays = 90;

  @Column(name = "requires_approval")
  @Builder.Default
  private Boolean requiresApproval = false;

  @Column(name = "allow_instant_booking")
  @Builder.Default
  private Boolean allowInstantBooking = true;
}
