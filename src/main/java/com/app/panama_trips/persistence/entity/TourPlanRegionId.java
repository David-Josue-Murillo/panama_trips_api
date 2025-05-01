package com.app.panama_trips.persistence.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class TourPlanRegionId implements Serializable {
    private Integer tourPlan;
    private Integer region;
}