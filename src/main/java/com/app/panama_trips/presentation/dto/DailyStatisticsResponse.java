package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.DailyStatistics;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.Region;
import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyStatisticsResponse(
    LocalDate date,
    Integer totalReservations,
    Integer completedReservations,
    Integer cancelledReservations,
    BigDecimal totalRevenue,
    Integer newUsers,
    TourPlan topTour,
    Region topRegion
) {
    public static DailyStatisticsResponse from(DailyStatistics entity) {
        if (entity == null) {
            return null;
        }
        return new DailyStatisticsResponse(
            entity.getDate(),
            entity.getTotalReservations(),
            entity.getCompletedReservations(),
            entity.getCancelledReservations(),
            entity.getTotalRevenue(),
            entity.getNewUsers(),
            entity.getTopTour(),
            entity.getTopRegion()
        );
    }
}