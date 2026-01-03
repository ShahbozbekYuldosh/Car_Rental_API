package com.carrental.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingRequest {
    @NotNull(message = "Mashina tanlanishi shart")
    private Long carId;

    @NotNull(message = "Olish vaqti shart")
    @Future(message = "Vaqt kelajakda bo'lishi kerak")
    private LocalDateTime pickUpTime;

    @NotNull(message = "Qaytarish vaqti shart")
    @Future(message = "Vaqt kelajakda bo'lishi kerak")
    private LocalDateTime dropOffTime;

    @NotBlank(message = "Olish manzili shart")
    private String pickUpLocation;

    @NotBlank(message = "Qaytarish manzili shart")
    private String dropOffLocation;
}