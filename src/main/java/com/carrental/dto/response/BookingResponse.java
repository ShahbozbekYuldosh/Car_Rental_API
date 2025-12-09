package com.carrental.dto.response;

import com.carrental.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private Long userId;
    private String userFullName;
    private Long carId;
    private String carBrand;
    private String carModel;
    private LocalDateTime pickupDate;
    private LocalDateTime returnDate;
    private String pickupLocation;
    private String returnLocation;
    private BigDecimal totalPrice;
    private BigDecimal deposit;
    private BookingStatus status;
    private String specialRequests;
    private LocalDateTime createdAt;
}

