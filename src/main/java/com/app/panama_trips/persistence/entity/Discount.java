package com.app.panama_trips.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "discounts")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false, foreignKey = @ForeignKey(name = "fk_discount_reservation"))
    private Reservation reservationId;

    @ManyToOne
    @JoinColumn(name = "coupon_id", foreignKey = @ForeignKey(name = "fk_discount_coupon"))
    private Coupon couponId;

    @Column(name = "discount_amount", nullable = false)
    private BigDecimal discountAmount;
}
