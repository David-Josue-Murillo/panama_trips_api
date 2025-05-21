package com.app.panama_trips.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_reservation_user"))
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "tour_plan_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_reservation_tour_plan"))
    private TourPlan tourPlan;

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status", nullable = false)
    @Builder.Default
    private ReservationStatus reservationStatus = ReservationStatus.pending;

    @Column(name = "reservation_date", nullable = false)
    private LocalDate reservationDate;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "number_of_persons", nullable = false, columnDefinition = "INTEGER DEFAULT 1")
    @Builder.Default
    private Integer numberOfPersons = 1;

    @Column(name = "number_of_children", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    @Builder.Default
    private Integer numberOfChildren = 0;

    @Column(name = "special_requests", columnDefinition = "TEXT")
    private String specialRequests;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    @ManyToOne
    @JoinColumn(name = "last_modified_by", foreignKey = @ForeignKey(name = "fk_reservation_last_modified_by"))
    private UserEntity lastModifiedBy;
}