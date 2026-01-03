package com.carrental.dto.response;

import com.carrental.entity.Booking;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Bron ma'lumotlari haqida javob")
public class BookingResponse {

    @Schema(description = "Bron ID raqami", example = "1")
    private Long id;

    @Schema(description = "Avtomobil modeli", example = "Malibu 2")
    private String carName;

    @Schema(description = "Davlat raqami", example = "01A777AA")
    private String registrationNumber;

    @Schema(description = "Olib ketish vaqti", example = "2025-12-25 10:00:00")
    private LocalDateTime pickUpTime;

    @Schema(description = "Qaytarish vaqti", example = "2025-12-26 10:00:00")
    private LocalDateTime dropOffTime;

    @Schema(description = "Olish manzili", example = "Toshkent Aeroport")
    private String pickUpLocation;

    @Schema(description = "Qaytarish manzili", example = "Yunusobod 4-mavze")
    private String dropOffLocation;

    @Schema(description = "Umumiy summa", example = "500000.0")
    private Double totalAmount;

    @Schema(description = "Bron holati", example = "PENDING")
    private String status;

    public static BookingResponse fromEntity(Booking booking) {
        if (booking == null) return null;

        return BookingResponse.builder()
                .id(booking.getId())
                .carName(booking.getCar().getVariant().getModel())
                .registrationNumber(booking.getCar().getRegistrationNumber())
                .pickUpTime(booking.getPickUpTime())
                .dropOffTime(booking.getDropOffTime())
                .totalAmount(booking.getTotalAmount())
                .pickUpLocation(booking.getPickUpLocation())
                .dropOffLocation(booking.getDropOffLocation())
                .status(booking.getStatus().name())
                .build();
    }
}