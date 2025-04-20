package com.app.panama_trips.persistence.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ReviewCategoryRatingId implements Serializable {

    private Integer reviewId;
    private Integer categoryId;
}