package com.carrental.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "car_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarCategoryEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name; // Economy, Compact, SUV, Luxury, etc.

    private String description;

    @OneToMany(mappedBy = "category")
    private Set<CarEntity> cars = new HashSet<>();
}
