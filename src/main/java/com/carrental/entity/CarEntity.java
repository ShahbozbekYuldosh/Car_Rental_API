package com.carrental.entity;

import com.carrental.enums.CarStatus;
import com.carrental.enums.FuelType;
import com.carrental.enums.TransmissionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarEntity extends BaseEntity {

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false, unique = true)
    private String plateNumber;

    private String color;

    @Enumerated(EnumType.STRING)
    private TransmissionType transmission;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    private Integer seats;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal dailyRate;

    private Integer mileage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarStatus status = CarStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CarCategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private LocationEntity location;

    private Boolean airConditioning = false;
    private Boolean gps = false;
    private Boolean bluetooth = false;
    private Boolean childSeat = false;

    private String description;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CarImageEntity> images = new HashSet<>();

    @OneToMany(mappedBy = "car")
    private Set<BookingEntity> bookings = new HashSet<>();

    @OneToMany(mappedBy = "car")
    private Set<ReviewEntity> reviews = new HashSet<>();
}