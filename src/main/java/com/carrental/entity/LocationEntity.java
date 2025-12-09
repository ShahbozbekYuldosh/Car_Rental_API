package com.carrental.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private String city;

    private String country;

    private String phone;

    private Boolean active = true;

    @OneToMany(mappedBy = "location")
    private Set<CarEntity> cars = new HashSet<>();

    @OneToMany(mappedBy = "pickupLocation")
    private Set<BookingEntity> pickupBookings = new HashSet<>();

    @OneToMany(mappedBy = "returnLocation")
    private Set<BookingEntity> returnBookings = new HashSet<>();
}
