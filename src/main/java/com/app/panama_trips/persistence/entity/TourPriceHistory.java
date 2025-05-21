package com.app.panama_trips.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tour_price_history")
public class TourPriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_plan_id", nullable = false, foreignKey = @ForeignKey(name = "fk_tour_price_history_tour_plan"))
    private TourPlan tourPlan;

    @Column(name = "previous_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal previousPrice;

    @Column(name = "new_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal newPrice;

    @Column(name = "changed_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime changedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by", foreignKey = @ForeignKey(name = "fk_tour_price_history_changed_by"))
    private UserEntity changedBy;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;
}