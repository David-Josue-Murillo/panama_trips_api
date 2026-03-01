package com.app.panama_trips.persistence.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TourPlanPricing {
  @Column(name = "price", nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @Column(name = "child_price", precision = 10, scale = 2)
  private BigDecimal childPrice;

  @Column(name = "currency", length = 3)
  @Builder.Default
  private String currency = "USD";

  @Column(name = "discount_percentage", precision = 5, scale = 2)
  @Builder.Default
  private BigDecimal discountPercentage = BigDecimal.ZERO;

  @Column(name = "tax_percentage", precision = 5, scale = 2)
  @Builder.Default
  private BigDecimal taxPercentage = BigDecimal.ZERO;
}
