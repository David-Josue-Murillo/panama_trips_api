package com.app.panama_trips.persistence.entity;

import com.app.panama_trips.persistence.entity.embeddable.*;
import com.app.panama_trips.persistence.entity.enums.DifficultyLevel;
import com.app.panama_trips.persistence.entity.enums.TourPlanStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tour_plans", indexes = {
        @Index(name = "idx_tour_plans_provider", columnList = "provider_id"),
        @Index(name = "idx_tour_plans_status", columnList = "status"),
        @Index(name = "idx_tour_plans_featured", columnList = "featured"),
        @Index(name = "idx_tour_plans_price", columnList = "price"),
        @Index(name = "idx_tour_plans_created_at", columnList = "created_at")
})
public class TourPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "slug", unique = true, length = 200)
    private String slug;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "short_description")
    private String shortDescription;

    @Embedded
    private TourPlanPricing pricing;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "available_spots", nullable = false)
    private Integer availableSpots;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    @Builder.Default
    private TourPlanStatus status = TourPlanStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_tour_created_by"))
    private UserEntity createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", foreignKey = @ForeignKey(name = "fk_tour_updated_by"))
    private UserEntity updatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false, foreignKey = @ForeignKey(name = "fk_tour_plan_provider"))
    private Provider provider;

    @Embedded
    private TourPlanSchedule schedule;

    @Embedded
    private TourPlanLogistics logistics;

    @Column(name = "max_capacity_per_day")
    private Integer maxCapacityPerDay;

    @Column(name = "min_participants")
    @Builder.Default
    private Integer minParticipants = 1;

    @Column(name = "max_participants")
    @Builder.Default
    private Integer maxParticipants = 10;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", length = 20)
    @Builder.Default
    private DifficultyLevel difficultyLevel = DifficultyLevel.EASY;

    @Column(name = "recommended_age", length = 20)
    @Builder.Default
    private String recommendedAge = "All ages";

    @Column(name = "wheelchair_accessible")
    @Builder.Default
    private Boolean wheelchairAccessible = false;

    @Column(name = "included_services", columnDefinition = "JSONB")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> includedServices;

    @Column(name = "excluded_services", columnDefinition = "JSONB")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> excludedServices;

    @Column(name = "what_to_bring", columnDefinition = "JSONB")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> whatToBring;

    @Column(name = "tags", columnDefinition = "JSONB")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> tags;

    @Column(columnDefinition = "JSONB DEFAULT '[\"es\", \"en\"]'")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> languageOptions;

    @Embedded
    private TourPlanMedia media;

    @Embedded
    private TourPlanSeo seo;

    @Column(name = "featured", nullable = false)
    @Builder.Default
    private Boolean featured = false;

    @Column(name = "featured_order")
    private Integer featuredOrder;

    @Column(name = "average_rating", precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name = "total_reviews")
    @Builder.Default
    private Integer totalReviews = 0;

    @Column(name = "total_bookings")
    @Builder.Default
    private Integer totalBookings = 0;

    @Column(name = "external_id", length = 100)
    private String externalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancellation_policy_id", foreignKey = @ForeignKey(name = "fk_tour_cancellation_policy"))
    private CancellationPolicy cancellationPolicy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
