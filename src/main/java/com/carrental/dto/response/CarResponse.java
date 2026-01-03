package com.carrental.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarResponse {
    private Long id;
    private String registrationNumber;
    private String color;
    private Double hourlyRate;
    private Double dailyRate;
    private String status;
    private String modelName;
    private String companyName;
    private List<String> imageUrls;
    private Integer doors;
    private Integer passengerCapacity;
    private Integer luggageCapacity;
    private Boolean hasAirConditioning;
    private String additionalFeatures;
}