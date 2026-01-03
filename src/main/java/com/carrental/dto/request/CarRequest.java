package com.carrental.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CarRequest {
    @NotBlank private String registrationNumber;
    @NotBlank private String color;
    @NotNull @Positive private Double hourlyRate;
    @NotNull @Positive private Double dailyRate;
    @Min(2) private Integer doors;
    @Min(1) private Integer passengerCapacity;
    @Min(0) private Integer luggageCapacity;
    private Boolean hasAirConditioning;
    private String additionalFeatures;
    @NotNull private Long variantId;
}
