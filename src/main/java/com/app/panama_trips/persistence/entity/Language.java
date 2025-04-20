package com.app.panama_trips.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "languages")
public class Language {

    @Id
    @Column(name = "code", length = 2)
    private String code;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "is_active")
    private Boolean isActive = true;
}