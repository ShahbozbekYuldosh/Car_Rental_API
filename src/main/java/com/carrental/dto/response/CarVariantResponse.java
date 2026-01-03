package com.carrental.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarVariantResponse {
    private Long id;
    private String model;
    private String variantName;
    private Integer year;
    private String fuelType;
    private String transmission;
    private Integer seatingCapacity;
    private String companyName;
    private Long companyId;
}