package com.carrental.utils;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class PriceCalculator {

    public BigDecimal calculateTotalPrice(BigDecimal dailyRate,
                                          LocalDateTime pickupDate,
                                          LocalDateTime returnDate) {
        long days = ChronoUnit.DAYS.between(pickupDate, returnDate);
        if (days < 1) days = 1;

        return dailyRate.multiply(BigDecimal.valueOf(days));
    }

    public BigDecimal calculateDeposit(BigDecimal totalPrice) {
        return totalPrice.multiply(BigDecimal.valueOf(0.2)); // 20%
    }

    public BigDecimal applyPromotion(BigDecimal totalPrice, BigDecimal discountPercentage) {
        BigDecimal discount = totalPrice.multiply(discountPercentage).divide(BigDecimal.valueOf(100));
        return totalPrice.subtract(discount);
    }
}
