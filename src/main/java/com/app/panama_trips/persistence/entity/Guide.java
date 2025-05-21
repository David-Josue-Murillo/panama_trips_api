package com.app.panama_trips.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "guides")
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_guide_user"))
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false, foreignKey = @ForeignKey(name = "fk_guide_provider"))
    private Provider provider;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "specialties", columnDefinition = "JSONB")
    private String specialties;

    @Column(name = "languages", columnDefinition = "JSONB")
    private String languages;

    @Column(name = "years_experience")
    private Integer yearsExperience;

    @Column(name = "certification_details", columnDefinition = "TEXT")
    private String certificationDetails;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}