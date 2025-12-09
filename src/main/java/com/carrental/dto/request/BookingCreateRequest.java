package com.carrental.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingCreateRequest {

    @NotNull(message = "Mashina ID si ko'rsatilishi shart")
    private Long carId;

    @NotNull(message = "Olish sanasi ko'rsatilishi shart")
    @Future(message = "Olish sanasi kelajakda bo'lishi kerak")
    private LocalDateTime pickupDate;

    @NotNull(message = "Qaytarish sanasi ko'rsatilishi shart")
    private LocalDateTime returnDate;

    @NotNull(message = "Olish joyi ko'rsatilishi shart")
    private Long pickupLocationId;

    @NotNull(message = "Qaytarish joyi ko'rsatilishi shart")
    private Long returnLocationId;

    private String specialRequests;

    private String promotionCode;
}
