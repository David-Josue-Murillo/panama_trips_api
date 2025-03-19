package com.app.panama_trips.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "providers")
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ruc", nullable = false, length = 25)
    private String ruc;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @ManyToOne
    @JoinColumn(name = "province_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_provider_province"))
    private Province province;

    @ManyToOne
    @JoinColumn(name = "district_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_provider_district"))
    private District district;

    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "address_id", foreignKey = @ForeignKey(name = "fk_provider_address"))
    private Address address;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
