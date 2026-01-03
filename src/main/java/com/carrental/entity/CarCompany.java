package com.carrental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "car_companies")
@Getter @Setter
public class CarCompany extends BaseEntity {
    private String name;
    private String country;
    private String website;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<CarVariant> variants;
}