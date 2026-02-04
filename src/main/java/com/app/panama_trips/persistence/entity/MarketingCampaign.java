package com.app.panama_trips.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import jakarta.persistence.GenerationType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.app.panama_trips.persistence.entity.enums.CampaignStatus;
import com.app.panama_trips.persistence.entity.enums.CampaignType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"coupon", "createdBy", "tours"})
@Entity
@Table(name = "marketing_campaigns", indexes = {
        @Index(columnList = "status"),
        @Index(columnList = "start_date"),
        @Index(columnList = "end_date"),
        @Index(columnList = "created_by")
    })
public class MarketingCampaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CampaignType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CampaignStatus status;

    @Column(name = "budget", nullable = false, precision = 12, scale = 2)
    private BigDecimal budget;

    @Column(name = "target_clicks")
    @Builder.Default
    private Long targetClicks = 0L;

    @Column(name = "actual_clicks")
    @Builder.Default
    private Long actualClicks = 0L;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "campaign_tours",
        joinColumns = @JoinColumn(name = "campaign_id"),
        inverseJoinColumns = @JoinColumn(name = "tour_id")
    )
    @Builder.Default
    private List<TourPlan> tours = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_code", referencedColumnName = "code", foreignKey = @ForeignKey(name = "fk_marketing_campaigns_discount_code"))
    private Coupon coupon;

    @Column(name = "target_audience", length = 50)
    private String targetAudience;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_marketing_campaigns_created_by"))
    private UserEntity createdBy;
}