package com.app.panama_trips.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tour_plan_images")
public class TourPlanImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_plan_id", nullable = false, foreignKey = @ForeignKey(name = "fk_tour_plan_image_tour_plan"))
    private TourPlan tourPlan;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    @Column(name = "alt_text", length = 100)
    private String altText;

    @Column(name = "is_main", nullable = false)
    private Boolean isMain = false;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0;
}