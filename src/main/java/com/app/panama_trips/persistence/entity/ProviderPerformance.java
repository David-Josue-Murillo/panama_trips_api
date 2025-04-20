package com.app.panama_trips.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "provider_performance")
@Immutable
public class ProviderPerformance {

    @Id
    @Column(name = "provider_id")
    private Integer providerId;

    @Column(name = "provider_name")
    private String providerName;

    @Column(name = "total_tours")
    private Long totalTours;

    @Column(name = "total_reservations")
    private Long totalReservations;

    @Column(name = "total_revenue", precision = 12, scale = 2)
    private BigDecimal totalRevenue;

    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating;

    @Column(name = "unique_customers")
    private Long uniqueCustomers;
}