package com.app.panama_trips.persistence.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.List;

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
  @JdbcTypeCode(SqlTypes.JSON)
  private List<String> imageGallery;

  @Column(name = "video_url")
  private String videoUrl;
}
