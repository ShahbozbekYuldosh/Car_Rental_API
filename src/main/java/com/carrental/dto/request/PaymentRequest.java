package com.carrental.dto.request;

import com.carrental.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {

    @NotNull(message = "Booking ID ko'rsatilishi shart")
    private Long bookingId;

    @NotNull(message = "Summa ko'rsatilishi shart")
    private BigDecimal amount;

    @NotNull(message = "To'lov usuli ko'rsatilishi shart")
    private PaymentMethod method;

    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;
}