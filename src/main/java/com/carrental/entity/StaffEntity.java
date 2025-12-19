package com.carrental.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "Staff")
public class StaffEntity extends BaseEntity {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Float salary;
    private Float commission;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private BranchEntity branch;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private AddressEntity address;
}
