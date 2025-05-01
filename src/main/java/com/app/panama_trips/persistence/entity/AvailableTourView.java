package com.app.panama_trips.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entity representing the available_tours database view.
 * This is a read-only entity as it maps to a database view.
 */
@Getter
@Entity
@Immutable
@Table(name = "available_tours")
public class AvailableTourView {

    @Id
    private Long id;

    private Long providerId;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private Integer availableSpots;
    private LocalDateTime createdAt;
    private String slug;
    private String shortDescription;
    private String status;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private LocalTime startTime;
    private LocalTime endTime;
    private String availableDays;
    private Boolean isSeasonal;
    private LocalDate seasonStartDate;
    private LocalDate seasonEndDate;
    private Integer maxCapacityPerDay;
    private String meetingPoint;
    private String meetingPointCoordinates;
    private String tourRoute;
    private BigDecimal childPrice;
    private Integer minParticipants;
    private Integer maxParticipants;
    private String currency;
    private BigDecimal discountPercentage;
    private BigDecimal taxPercentage;
    private Integer bookingDeadlineHours;
    private Integer minAdvanceBookingDays;
    private Integer maxAdvanceBookingDays;
    private Boolean requiresApproval;
    private Boolean allowInstantBooking;
    private String difficultyLevel;
    private String recommendedAge;
    private Boolean wheelchairAccessible;
    private String includedServices;
    private String excludedServices;
    private String whatToBring;
    private String tags;
    private String languageOptions;
    private String mainImageUrl;
    private String thumbnailUrl;
    private String imageGallery;
    private String videoUrl;
    private String seoTitle;
    private String seoDescription;
    private String seoKeywords;
    private Boolean featured;
    private Integer featuredOrder;
    private BigDecimal averageRating;
    private Integer totalReviews;
    private Integer totalBookings;
    private String externalId;

    // Fields calculated by the view
    private Long activeBookings;
    private Long remainingSpots;

    // Default constructor required by JPA
    public AvailableTourView() {
    }

    @Override
    public String toString() {
        return "AvailableTourView{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", remainingSpots=" + remainingSpots +
                '}';
    }
}
