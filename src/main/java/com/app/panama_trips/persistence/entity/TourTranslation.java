package com.app.panama_trips.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tour_translations")
public class TourTranslation {

    @EmbeddedId
    private TourTranslationId id;

    @MapsId("tourPlanId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_plan_id")
    private TourPlan tourPlan;

    @MapsId("languageCode")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_code")
    private Language language;

    @Column(name = "title", length = 150)
    private String title;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "included_services", columnDefinition = "JSONB")
    private String includedServices;

    @Column(name = "excluded_services", columnDefinition = "JSONB")
    private String excludedServices;

    @Column(name = "what_to_bring", columnDefinition = "JSONB")
    private String whatToBring;

    @Column(name = "meeting_point")
    private String meetingPoint;
}