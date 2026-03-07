package com.app.panama_trips.persistence.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TourPlanSeo {
  @Column(name = "seo_title", length = 100)
  private String seoTitle;

  @Column(name = "seo_description")
  private String seoDescription;

  @Column(name = "seo_keywords")
  private String seoKeywords;
}
