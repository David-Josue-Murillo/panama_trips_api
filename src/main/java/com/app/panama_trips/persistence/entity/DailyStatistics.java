package com.app.panama_trips.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "daily_statistics")
public class DailyStatistics {

    @Id
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "total_reservations")
    private Integer totalReservations = 0;

    @Column(name = "completed_reservations")
    private Integer completedReservations = 0;

    @Column(name = "cancelled_reservations")
    private Integer cancelledReservations = 0;

    @Column(name = "total_revenue", precision = 12, scale = 2)
    private BigDecimal totalRevenue = BigDecimal.ZERO;

    @Column(name = "new_users")
    private Integer newUsers = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "top_tour_id", foreignKey = @ForeignKey(name = "fk_daily_statistics_top_tour"))
    private TourPlan topTour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "top_region_id", foreignKey = @ForeignKey(name = "fk_daily_statistics_top_region"))
    private Region topRegion;
}