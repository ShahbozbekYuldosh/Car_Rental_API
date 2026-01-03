package com.carrental.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {
    @NotNull
    private Long bookingId;
    @NotNull
    private String paymentMethod;
    @NotNull
    private String transactionId;
}