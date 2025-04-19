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
@Table(name = "tour_plan_special_prices", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tour_plan_id", "start_date", "end_date"})
})
public class TourPlanSpecialPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_plan_id", nullable = false, foreignKey = @ForeignKey(name = "fk_tour_plan_special_price_tour_plan"))
    private TourPlan tourPlan;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "description", length = 100)
    private String description;
}