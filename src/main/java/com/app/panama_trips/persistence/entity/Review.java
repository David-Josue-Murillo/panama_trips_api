package com.app.panama_trips.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_review_user"))
    private UserEntity userId;

    @ManyToOne
    @JoinColumn(name = "tour_plan_id", nullable = false, foreignKey = @ForeignKey(name = "fk_review_tour_plan"))
    private TourPlan tourPlanId;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "verified_purchase")
    private Boolean verifiedPurchase = false;

    @Column(name = "response_by_provider", columnDefinition = "TEXT")
    private String responseByProvider;

    @Column(name = "response_date")
    private LocalDateTime responseDate;

    @Column(name = "helpful_votes")
    private Integer helpfulVotes = 0;

    @Column(name = "reported")
    private Boolean reported = false;

    @Column(name = "status", length = 20)
    private String status = "ACTIVE";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}