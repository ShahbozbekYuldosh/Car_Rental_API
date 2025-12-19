package com.carrental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cost")
@Getter
@Setter
public class CostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long costId;

    private String costClass;
    private Integer costPerDay;
    private Integer costPerKilometer;
    private Integer securityDeposit;
}

