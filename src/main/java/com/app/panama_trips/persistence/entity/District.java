package com.app.panama_trips.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "districts")
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name", nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", nullable = false, foreignKey = @ForeignKey(name = "fk_district_province"))
    private Province province;

    @OneToMany(mappedBy = "district", fetch = FetchType.LAZY)
    private List<Address> addresses;
}
