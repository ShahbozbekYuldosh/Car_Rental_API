package com.carrental.dto.request;

import com.carrental.enums.FuelType;
import com.carrental.enums.TransmissionType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CarVariantRequest {
    @NotBlank(message = "Model nomi bo'sh bo'lmasligi kerak")
    private String model;

    @NotBlank(message = "Variant nomi bo'sh bo'lmasligi kerak")
    private String variantName;

    @Min(1900) @Max(2100)
    private Integer year;

    @NotNull(message = "Yoqilg'i turi tanlanishi shart")
    private FuelType fuelType;

    @NotNull(message = "Transmissiya turi tanlanishi shart")
    private TransmissionType transmission;

    @Min(1) @Max(100)
    private Integer seatingCapacity;

    @NotNull(message = "Kompaniya tanlanishi shart")
    private Long companyId;
}