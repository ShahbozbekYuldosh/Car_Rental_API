package com.carrental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "car_variants")
@Getter @Setter
public class CarVariant extends BaseEntity {
    private String model;
    private String variantName;
    private Integer year;
    private String fuelType;
    private String transmission;
    private Integer seatingCapacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CarCompany company;
}

