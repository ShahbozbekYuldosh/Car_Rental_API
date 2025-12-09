package com.carrental.dto.request;
import com.carrental.enums.FuelType;
import com.carrental.enums.TransmissionType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarCreateRequest {

    @NotBlank(message = "Brend bo'sh bo'lishi mumkin emas")
    private String brand;

    @NotBlank(message = "Model bo'sh bo'lishi mumkin emas")
    private String model;

    @NotNull(message = "Yil ko'rsatilishi shart")
    @Min(value = 2000, message = "Yil 2000 dan kichik bo'lmasligi kerak")
    private Integer year;

    @NotBlank(message = "Davlat raqami bo'sh bo'lishi mumkin emas")
    private String plateNumber;

    private String color;

    @NotNull(message = "Transmissiya turi ko'rsatilishi shart")
    private TransmissionType transmission;

    @NotNull(message = "Yoqilg'i turi ko'rsatilishi shart")
    private FuelType fuelType;

    @NotNull(message = "O'rindiqlar soni ko'rsatilishi shart")
    @Min(value = 2, message = "Kamida 2 ta o'rindiq bo'lishi kerak")
    private Integer seats;

    @NotNull(message = "Kunlik narx ko'rsatilishi shart")
    @DecimalMin(value = "0.0", inclusive = false, message = "Narx 0 dan katta bo'lishi kerak")
    private BigDecimal dailyRate;

    private Integer mileage;

    @NotNull(message = "Kategoriya ID si ko'rsatilishi shart")
    private Long categoryId;

    @NotNull(message = "Joylashuv ID si ko'rsatilishi shart")
    private Long locationId;

    private Boolean airConditioning;
    private Boolean gps;
    private Boolean bluetooth;
    private Boolean childSeat;
    private String description;
}