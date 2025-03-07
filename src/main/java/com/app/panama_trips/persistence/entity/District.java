package com.app.panama_trips.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_district_province"))
    private Province province;
}
