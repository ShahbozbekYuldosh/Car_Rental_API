package com.carrental.dto.response;

import com.carrental.enums.CarStatus;
import com.carrental.enums.FuelType;
import com.carrental.enums.TransmissionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarResponse {
    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private String plateNumber;
    private String color;
    private TransmissionType transmission;
    private FuelType fuelType;
    private Integer seats;
    private BigDecimal dailyRate;
    private Integer mileage;
    private CarStatus status;
    private String categoryName;
    private String locationName;
    private Boolean airConditioning;
    private Boolean gps;
    private Boolean bluetooth;
    private Boolean childSeat;
    private String description;
    private List<String> imageUrls;
    private Double averageRating;
    private Long reviewCount;
}
