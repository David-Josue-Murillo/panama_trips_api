package com.app.panama_trips.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tour_plan_regions")
public class TourPlanRegion {

    @Id
    @ManyToOne
    @JoinColumn(name = "tour_plan_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_tour_plan_region_tour_plan"))
    private TourPlan tourPlan;

    @Id
    @ManyToOne
    @JoinColumn(name = "region_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_tour_plan_region_region"))
    private Region region;
}
