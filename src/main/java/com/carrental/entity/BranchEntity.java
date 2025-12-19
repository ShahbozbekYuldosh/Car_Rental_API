package com.carrental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "branch")
@Getter
@Setter
public class BranchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long branchId;

    private Integer parkingSpaces;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private StaffEntity manager;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private AddressEntity address;
}

