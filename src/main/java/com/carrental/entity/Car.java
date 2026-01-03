package com.carrental.entity;

import com.carrental.enums.CarStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars")
@Getter @Setter
public class Car extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String registrationNumber;
    private String color;
    private Double hourlyRate;
    private Double dailyRate;
    private Integer doors;
    private Integer passengerCapacity;
    private Integer luggageCapacity;
    private Boolean hasAirConditioning;
    private String additionalFeatures;

    @Enumerated(EnumType.STRING)
    private CarStatus status = CarStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    private CarVariant variant;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarImage> images = new ArrayList<>();
}