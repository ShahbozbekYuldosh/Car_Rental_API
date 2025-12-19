package com.carrental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payment")
@Getter
@Setter
public class PaymentEntity {

    @EmbeddedId
    private PaymentId id;

    private Integer paymentAmount;

    @MapsId("rentId")
    @ManyToOne
    @JoinColumn(name = "rent_id")
    private RentEntity rent;
}