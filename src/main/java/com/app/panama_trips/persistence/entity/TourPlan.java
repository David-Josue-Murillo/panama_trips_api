package com.app.panama_trips.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "available_spots", nullable = false)
    private Integer availableSpots;

    @Column(name = "status", length = 20)
    @Builder.Default
    private String status = "ACTIVE";

    @ManyToOne
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_tour_created_by"))
    private UserEntity createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by", foreignKey = @ForeignKey(name = "fk_tour_updated_by"))
    private UserEntity updatedBy;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false, foreignKey = @ForeignKey(name = "fk_tour_plan_provider"))
    private Provider provider;

    @Column(name = "start_time", length = 8)
    private String startTime;

    @Column(name = "end_time", length = 8)
    private String endTime;

    @Column(name = "available_days", columnDefinition = "JSONB DEFAULT '[\"MON\", \"TUE\", \"WED\", \"THU\", \"FRI\", \"SAT\", \"SUN\"]'")
    private String availableDays;

    @Column(name = "is_seasonal")
    @Builder.Default
    private Boolean isSeasonal = false;

    @Column(name = "season_start_date")
    private LocalDate seasonStartDate;

    @Column(name = "season_end_date")
    private LocalDate seasonEndDate;

    @Column(name = "max_capacity_per_day")
    private Integer maxCapacityPerDay;

    @Column(name = "meeting_point")
    private String meetingPoint;

    @Column(name = "meeting_point_coordinates", length = 100)
    private String meetingPointCoordinates;

    @Column(columnDefinition = "text")
    private String tourRoute;

    @Column(name = "child_price", precision = 10, scale = 2)
    private BigDecimal childPrice;

    @Column(name = "min_participants")
    @Builder.Default
    private Integer minParticipants = 1;

    @Column(name = "max_participants")
    @Builder.Default
    private Integer maxParticipants = 10;

    @Column(name = "currency", length = 3)
    @Builder.Default
    private String currency = "USD";

    @Column(name = "discount_percentage", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal discountPercentage = BigDecimal.ZERO;

    @Column(name = "tax_percentage", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal taxPercentage = BigDecimal.ZERO;

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

    @Column(name = "difficulty_level", length = 20)
    @Builder.Default
    private String difficultyLevel = "EASY";

    @Column(name = "recommended_age", length = 20)
    @Builder.Default
    private String recommendedAge = "All ages";

    @Column(name = "wheelchair_accessible")
    @Builder.Default
    private Boolean wheelchairAccessible = false;

    @Column(name = "included_services", columnDefinition = "JSONB")
    private String includedServices;

    @Column(name = "excluded_services", columnDefinition = "JSONB")
    private String excludedServices;

    @Column(name = "what_to_bring", columnDefinition = "JSONB")
    private String whatToBring;

    @Column(name = "tags", columnDefinition = "JSONB")
    private String tags;

    @Column(columnDefinition = "JSONB DEFAULT '[\"es\", \"en\"]'")
    private String languageOptions;

    @Column(name = "main_image_url")
    private String mainImageUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "image_gallery", columnDefinition = "JSONB")
    private String imageGallery;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "seo_title", length = 100)
    private String seoTitle;

    @Column(name = "seo_description")
    private String seoDescription;

    @Column(name = "seo_keywords")
    private String seoKeywords;

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

    @ManyToOne
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
