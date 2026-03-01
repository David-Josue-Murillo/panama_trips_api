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
public class TourPlanMedia {
  @Column(name = "main_image_url")
  private String mainImageUrl;

  @Column(name = "thumbnail_url")
  private String thumbnailUrl;

  @Column(name = "image_gallery", columnDefinition = "JSONB")
  private String imageGallery;

  @Column(name = "video_url")
  private String videoUrl;
}
