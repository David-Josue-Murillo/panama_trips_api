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
@Table(name = "tour_plan_availability", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tour_plan_id", "available_date"})
})
public class TourPlanAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_plan_id", nullable = false, foreignKey = @ForeignKey(name = "fk_tour_plan_availability_tour_plan"))
    private TourPlan tourPlan;

    @Column(name = "available_date", nullable = false)
    private LocalDate availableDate;

    @Column(name = "available_spots", nullable = false)
    private Integer availableSpots;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @Column(name = "price_override", precision = 10, scale = 2)
    private BigDecimal priceOverride;
}
