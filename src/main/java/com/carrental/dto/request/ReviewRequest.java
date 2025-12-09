package com.carrental.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewRequest {

    @NotNull(message = "Booking ID ko'rsatilishi shart")
    private Long bookingId;

    @NotNull(message = "Reyting ko'rsatilishi shart")
    @Min(value = 1, message = "Reyting kamida 1 bo'lishi kerak")
    @Max(value = 5, message = "Reyting ko'pi bilan 5 bo'lishi kerak")
    private Integer rating;

    @Size(max = 1000, message = "Izoh 1000 belgidan oshmasligi kerak")
    private String comment;
}