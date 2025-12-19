package com.carrental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "vehicle")
@Getter
@Setter
public class CarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;

    private String brand;
    private Integer mileage;
    private LocalDate dateBought;
    private Boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "cost_id")
    private CostEntity cost;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private BranchEntity branch;
}

