package com.carrental.entity;

import com.carrental.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private CarEntity car;

    @Column(nullable = false)
    private LocalDateTime pickupDate;

    @Column(nullable = false)
    private LocalDateTime returnDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_location_id")
    private LocationEntity pickupLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_location_id")
    private LocationEntity returnLocation;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal deposit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    private String specialRequests;

    private LocalDateTime actualReturnDate;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private PaymentEntity payment;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private ReviewEntity review;
}