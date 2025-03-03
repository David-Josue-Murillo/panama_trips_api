package com.app.panama_trips.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "regions")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "province_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_region_province"))
    private Province provinceId;

    @ManyToOne
    @JoinColumn(name = "comarca_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_region_comarca"))
    private Comarca comarca;
}
