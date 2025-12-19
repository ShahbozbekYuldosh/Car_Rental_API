package com.carrental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "rent")
@Getter
@Setter
public class RentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentId;

    private Integer tripDuration;
    private Integer freeKilometers;
    private Boolean isReturned;
    private LocalDate dateRented;
    private LocalDate dateReturned;
    private Integer mileageReturned;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private CarEntity vehicle;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private StaffEntity staff;
}

