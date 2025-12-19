package com.carrental.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Getter
@Setter
public class PaymentId implements Serializable {

    private Long rentId;
    private LocalDate paymentDate;
}
